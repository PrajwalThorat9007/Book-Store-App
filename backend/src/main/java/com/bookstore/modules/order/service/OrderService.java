package com.bookstore.modules.order.service;

/*
 * This is the service layer class for the Order module.
 * It contains all business logic for placing, viewing, cancelling, and managing orders.
 * When an order is placed, it reads the cart, deducts stock, saves order items, and clears the cart.
 * When an order is cancelled, it restores the stock for each item.
 * Ownership checks ensure users can only view or cancel their own orders.
 */

import com.bookstore.common.OrderStatus;
import com.bookstore.entity.*;
import com.bookstore.modules.cart.repository.CartItemRepository;
import com.bookstore.modules.cart.repository.CartRepository;
import com.bookstore.modules.customer.repository.AddressRepository;
import com.bookstore.modules.order.dto.*;
import com.bookstore.modules.order.repository.OrderItemRepository;
import com.bookstore.modules.order.repository.OrderRepository;
import com.bookstore.modules.product.repository.ProductRepository;
import com.bookstore.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    // Places a new order — reads cart items, validates stock, deducts stock, saves order, clears cart
    @Transactional
    public OrderResponse placeOrder(String email, OrderRequest orderRequest) {
        log.info("Placing order for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No active cart found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        // Cannot place an order with an empty cart
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order: cart is empty");
        }

        Address deliveryAddress = addressRepository.findById(orderRequest.getDeliveryAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Ensure the delivery address belongs to this user — prevents using someone else's address
        if (!deliveryAddress.getCustomerProfile().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Address does not belong to this user");
        }

        // Build order items and deduct stock for each product
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            int qty = cartItem.getQuantity();

            if (product.getStockQuantity() < qty) {
                throw new RuntimeException("Insufficient stock for: " + product.getTitle());
            }

            // Deduct the ordered quantity from available stock
            product.setStockQuantity(product.getStockQuantity() - qty);
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(qty);
            item.setUnitPrice(product.getPrice());
            return item;
        }).collect(Collectors.toList());

        // Calculate the total order amount from all items
        BigDecimal total = orderItems.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Save the order header first, then link items to it
        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING.name());
        Order savedOrder = orderRepository.save(order);

        // Assign the saved order to each item and persist them
        orderItems.forEach(item -> item.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);

        // Clear the cart after the order is placed
        cartItemRepository.deleteAll(cartItems);
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

        log.info("Order {} placed successfully for user: {}", savedOrder.getId(), email);
        return mapToOrderResponse(savedOrder, orderItems);
    }

    // Returns all orders for a user sorted newest first
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(order -> mapToOrderResponse(order, orderItemRepository.findByOrder(order)))
                .collect(Collectors.toList());
    }

    // Returns a single order — ownership check ensures users can't view other users' orders
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        return mapToOrderResponse(order, orderItemRepository.findByOrder(order));
    }

    // Cancels an order — only PENDING orders can be cancelled, stock is restored on cancellation
    @Transactional
    public OrderResponse cancelOrder(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // Only PENDING orders can be cancelled — reject if already shipped or delivered
        OrderStatus currentStatus = OrderStatus.fromString(order.getStatus());
        if (currentStatus == null || !currentStatus.isCancellable()) {
            throw new RuntimeException(
                    "Only PENDING orders can be cancelled. Current status: " + order.getStatus());
        }

        // Restore stock for each item in the cancelled order
        List<OrderItem> items = orderItemRepository.findByOrder(order);
        items.forEach(item -> {
            Product p = item.getProduct();
            p.setStockQuantity(p.getStockQuantity() + item.getQuantity());
            productRepository.save(p);
        });

        order.setStatus(OrderStatus.CANCELLED.name());
        log.info("Order {} cancelled for user: {}", orderId, email);
        return mapToOrderResponse(orderRepository.save(order), items);
    }

    // Returns all orders in the system — admin use only
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> mapToOrderResponse(order, orderItemRepository.findByOrder(order)))
                .collect(Collectors.toList());
    }

    // Updates the status of any order — admin use only, no ownership check needed
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest req) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        log.info("Order {} status: {} → {}", orderId, order.getStatus(), req.getStatus());
        order.setStatus(req.getStatus());
        Order saved = orderRepository.save(order);

        return mapToOrderResponse(saved, orderItemRepository.findByOrder(saved));
    }

    // Converts an Order entity and its items into an OrderResponse DTO — flattens address into a string
    private OrderResponse mapToOrderResponse(Order order, List<OrderItem> items) {
        OrderResponse r = new OrderResponse();
        r.setId(order.getId());
        r.setUserId(order.getUser().getId());
        r.setUserName(order.getUser().getName());
        r.setTotalAmount(order.getTotalAmount());
        r.setStatus(order.getStatus());
        r.setCreatedAt(order.getCreatedAt());

        // Flatten address fields into a single readable string for the response
        Address a = order.getDeliveryAddress();
        r.setDeliveryAddress(a.getLine1() + ", " + a.getCity() + ", "
                + a.getState() + " " + a.getPincode());

        r.setItems(items.stream().map(item -> {
            OrderItemResponse i = new OrderItemResponse();
            i.setId(item.getId());
            i.setProductId(item.getProduct().getId());
            i.setProductTitle(item.getProduct().getTitle());
            i.setQuantity(item.getQuantity());
            i.setUnitPrice(item.getUnitPrice());
            // Subtotal is calculated here — not stored in the database
            i.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            return i;
        }).collect(Collectors.toList()));

        return r;
    }
}
