package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a book category used to group products
 */
@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;
}
