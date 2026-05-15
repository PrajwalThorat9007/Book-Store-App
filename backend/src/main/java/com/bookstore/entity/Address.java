package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a saved delivery address belonging to a customer profile
 */
@Data
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_profile_id", nullable = false)
    private CustomerProfile customerProfile;

    @Column(nullable = false)
    private String line1;

    @Column
    private String line2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String pincode;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
}
