// Manas: Module changed → modules/order/service
// What's changed: New file. Full business logic for all order operations.
//                 placeOrder, getOrdersByUser, getOrderById,
//                 cancelOrder, getAllOrders, updateOrderStatus.

package com.bookstore.modules.order.service;

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

    // ── 1. PLACE ORDER ───────────────────────────────────────────
    @Transactional
    public OrderResponse placeOrder(String email, OrderRequest orderRequest) {
        log.info("Placing order for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No active cart found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order: cart is empty");
        }

        Address deliveryAddress = addressRepository.findById(orderRequest.getDeliveryAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!deliveryAddress.getCustomerProfile().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Address does not belong to this user");
        }

        // Build order items + deduct stock
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            int qty = cartItem.getQuantity();

            if (product.getStockQuantity() < qty) {
                throw new RuntimeException("Insufficient stock for: " + product.getTitle());
            }

            product.setStockQuantity(product.getStockQuantity() - qty);
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(qty);
            item.setUnitPrice(product.getPrice());
            return item;
        }).collect(Collectors.toList());

        // Calculate total
        BigDecimal total = orderItems.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Save order
        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        Order savedOrder = orderRepository.save(order);

        // Save items
        orderItems.forEach(item -> item.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);

        // Clear cart
        cartItemRepository.deleteAll(cartItems);
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

        log.info("Order {} placed successfully for user: {}", savedOrder.getId(), email);
        return mapToOrderResponse(savedOrder, orderItems);
    }

    // ── 2. GET USER ORDER HISTORY ────────────────────────────────
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(order -> mapToOrderResponse(order, orderItemRepository.findByOrder(order)))
                .collect(Collectors.toList());
    }

    // ── 3. GET SINGLE ORDER (with ownership check) ───────────────
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        return mapToOrderResponse(order, orderItemRepository.findByOrder(order));
    }

    // ── 4. CANCEL ORDER ──────────────────────────────────────────
    @Transactional
    public OrderResponse cancelOrder(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException(
                    "Only PENDING orders can be cancelled. Current status: " + order.getStatus());
        }

        // Restore stock
        List<OrderItem> items = orderItemRepository.findByOrder(order);
        items.forEach(item -> {
            Product p = item.getProduct();
            p.setStockQuantity(p.getStockQuantity() + item.getQuantity());
            productRepository.save(p);
        });

        order.setStatus("CANCELLED");
        log.info("Order {} cancelled for user: {}", orderId, email);
        return mapToOrderResponse(orderRepository.save(order), items);
    }

    // ── 5. GET ALL ORDERS (ADMIN) ────────────────────────────────
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> mapToOrderResponse(order, orderItemRepository.findByOrder(order)))
                .collect(Collectors.toList());
    }

    // ── 6. UPDATE ORDER STATUS (ADMIN) ───────────────────────────
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest req) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        log.info("Order {} status: {} → {}", orderId, order.getStatus(), req.getStatus());
        order.setStatus(req.getStatus());
        Order saved = orderRepository.save(order);

        return mapToOrderResponse(saved, orderItemRepository.findByOrder(saved));
    }

    // ── PRIVATE MAPPER ───────────────────────────────────────────
    private OrderResponse mapToOrderResponse(Order order, List<OrderItem> items) {
        OrderResponse r = new OrderResponse();
        r.setId(order.getId());
        r.setUserId(order.getUser().getId());
        r.setUserName(order.getUser().getName());
        r.setTotalAmount(order.getTotalAmount());
        r.setStatus(order.getStatus());
        r.setCreatedAt(order.getCreatedAt());

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
            i.setSubtotal(item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
            return i;
        }).collect(Collectors.toList()));

        return r;
    }
}