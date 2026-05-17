package com.bookstore.common;

/*
 * This enum defines all valid order status values in the bookstore system.
 * Using an enum instead of raw strings prevents typos and makes status transitions explicit.
 * Each constant has a human-readable display label and a description of what it means.
 *
 * Normal flow:  PENDING → CONFIRMED → SHIPPED → DELIVERED
 * Cancel flow:  PENDING → CANCELLED  (only PENDING orders can be cancelled by the user)
 * Admin can cancel or refund at any stage.
 */
public enum OrderStatus {

    // Order has been placed but not yet reviewed by admin
    PENDING("Pending"),

    // Admin has confirmed the order and it is being prepared
    CONFIRMED("Confirmed"),

    // Order has been handed over to the delivery partner
    SHIPPED("Shipped"),

    // Order has been successfully delivered to the customer
    DELIVERED("Delivered"),

    // Order was cancelled — either by the user (only if PENDING) or by admin
    CANCELLED("Cancelled"),

    // Order amount has been refunded — typically after cancellation of a paid order
    REFUNDED("Refunded");

    // Human-readable label used in API responses and logs
    private final String displayName;

    // Constructor assigns the display label to each enum constant
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    // Returns the display label — e.g. "Pending" instead of "PENDING"
    public String getDisplayName() {
        return displayName;
    }

    // Returns true if the order can still be cancelled by the user
    // Only PENDING orders are cancellable — once confirmed or shipped it's too late
    public boolean isCancellable() {
        return this == PENDING;
    }

    // Checks whether a transition from the current status to the given new status is valid
    // Enforces the allowed status flow — prevents illegal jumps like DELIVERED → PENDING
    public boolean canTransitionTo(OrderStatus newStatus) {
        switch (this) {
            case PENDING:
                // From PENDING: can confirm or cancel
                return newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED:
                // From CONFIRMED: can ship or cancel (admin decision)
                return newStatus == SHIPPED || newStatus == CANCELLED;
            case SHIPPED:
                // From SHIPPED: can only mark as delivered or cancelled (rare edge case)
                return newStatus == DELIVERED || newStatus == CANCELLED;
            case DELIVERED:
                // From DELIVERED: can only refund
                return newStatus == REFUNDED;
            case CANCELLED:
            case REFUNDED:
                // Terminal states — no further transitions allowed
                return false;
            default:
                return false;
        }
    }

    // Converts a plain string (e.g. from DB or request) to the matching enum constant
    // Returns null if the string doesn't match any known status — caller should handle null
    public static OrderStatus fromString(String status) {
        if (status == null) return null;
        for (OrderStatus s : values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null;
    }
}
