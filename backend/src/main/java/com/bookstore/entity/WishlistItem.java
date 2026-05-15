package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents an individual book saved inside a user's wishlist
 */
@Data
@Entity
@Table(
    name = "wishlist_items",
    uniqueConstraints = @UniqueConstraint(columnNames = {"wishlist_id", "product_id"})
)
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
