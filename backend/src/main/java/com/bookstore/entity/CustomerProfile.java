package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents customer delivery details and preferences linked to a user
 */
@Data
@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column
    private String phone;

    @Lob
    @Column(name = "preference_notes")
    private String preferenceNotes;
}
