// Manas: Module changed → modules/order/controller
// What's changed: Full OrderController implemented with all USER and ADMIN endpoints,
//                 security annotations, validation, and proper HTTP status codes.

package com.bookstore.modules.order.controller;

import com.bookstore.modules.order.dto.OrderRequest;
import com.bookstore.modules.order.dto.OrderResponse;
import com.bookstore.modules.order.dto.OrderStatusUpdateRequest;
import com.bookstore.modules.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Order Module
 *
 * Handles all HTTP requests related to:
 * - Placing a new order (USER)
 * - Viewing own order history (USER)
 * - Viewing a specific order's details (USER)
 * - Cancelling a PENDING order (USER)
 * - Viewing all orders (ADMIN)
 * - Updating order status (ADMIN)
 *
 * Base URL: /api/orders
 *
 * Security:
 * - USER endpoints require ROLE_USER (authenticated customer)
 * - ADMIN endpoints require ROLE_ADMIN
 * - userId is extracted from JWT via SecurityContext (@AuthenticationPrincipal)
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor // Manas: Lombok generates constructor injection for OrderService
public class OrderController {

    // Manas: OrderService will contain all business logic — controller stays thin
    private final OrderService orderService;


    // USER ENDPOINTS
    /**
     * POST /api/orders
     *
     * Places a new order from the authenticated user's cart.
     * The cart must not be empty — validation is handled in OrderService.
     *
     * @param userDetails  Injected from JWT SecurityContext (logged-in user info)
     * @param orderRequest Contains the delivery address ID chosen by the user
     * @return 201 Created + the created OrderResponse
     *
     * Manas: @Valid triggers Jakarta Bean Validation on OrderRequest fields
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderRequest orderRequest) {

        // Manas: We extract the username/email from the JWT principal
        //        OrderService uses it to find the User entity internally
        String email = userDetails.getUsername();
        OrderResponse response = orderService.placeOrder(email, orderRequest);

        // Manas: 201 Created is the correct HTTP status for a new resource being created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/orders
     *
     * Returns the full order history of the currently logged-in user.
     *
     * @param userDetails Injected from JWT SecurityContext
     * @return 200 OK + List of OrderResponse for this user
     *
     * Manas: No request body needed — user is identified via JWT
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<OrderResponse> orders = orderService.getOrdersByUser(email);

        return ResponseEntity.ok(orders);
    }

    /**
     * GET /api/orders/{id}
     *
     * Returns the details of one specific order.
     * The service layer will verify that the order belongs to this user
     * (so a user cannot view someone else's order).
     *
     * @param userDetails Injected from JWT SecurityContext
     * @param id          Order ID from the URL path
     * @return 200 OK + OrderResponse with full order details including items
     *
     * Manas: @PathVariable binds {id} from the URL to the Long id parameter
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        String email = userDetails.getUsername();
        OrderResponse response = orderService.getOrderById(email, id);

        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/orders/{id}/cancel
     *
     * Cancels an order — but ONLY if its current status is "PENDING".
     * If the order is already SHIPPED or DELIVERED, the service throws
     * a BadRequestException (or similar), which returns 400 or 403.
     *
     * @param userDetails Injected from JWT SecurityContext
     * @param id          Order ID to cancel
     * @return 200 OK + updated OrderResponse showing status = "CANCELLED"
     *
     * Manas: Status check (PENDING only) is enforced in OrderService, not here.
     *        Controller remains thin — no if/else business logic here.
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        String email = userDetails.getUsername();
        OrderResponse response = orderService.cancelOrder(email, id);

        return ResponseEntity.ok(response);
    }

 //ADMIN ENDPOINTS

    /**
     * GET /api/orders/all
     *
     * Admin-only: Returns every order in the system, across all users.
     * Useful for the admin dashboard to track all activity.
     *
     * @return 200 OK + List of ALL OrderResponse objects
     *
     * Manas: ADMIN role required — @PreAuthorize blocks any non-admin JWT
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> orders = orderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }

    /**
     * PUT /api/orders/{id}/status
     *
     * Admin-only: Updates the status of any order.
     * Valid status transitions: PENDING → CONFIRMED → SHIPPED → DELIVERED
     * (or PENDING → CANCELLED by admin)
     *
     * @param id                      Order ID to update
     * @param orderStatusUpdateRequest Contains the new status string
     * @return 200 OK + updated OrderResponse with new status
     *
     * Manas: @Valid ensures the new status in the request body is not blank/null
     *        Actual allowed values are validated in OrderService
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest orderStatusUpdateRequest) {

        OrderResponse response = orderService.updateOrderStatus(id, orderStatusUpdateRequest);

        return ResponseEntity.ok(response);
    }
}