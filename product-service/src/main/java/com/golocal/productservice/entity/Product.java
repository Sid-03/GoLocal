package com.golocal.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp; // Optional: for creation timestamp
import org.hibernate.annotations.UpdateTimestamp;   // Optional: for update timestamp

import java.time.Instant; // Optional: for timestamps

/**
 * JPA Entity representing a Product in the database.
 */
@Data // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: No-args constructor needed by JPA
@AllArgsConstructor // Lombok: All-args constructor
@Entity
@Table(name = "product", // Explicitly name the table
        indexes = {
                @Index(name = "idx_product_name", columnList = "name"), // Index on name for searching
                @Index(name = "idx_product_supplier", columnList = "supplierName") // Index on supplier name
        },
        uniqueConstraints = {
   		@UniqueConstraint(columnNames = "name", name = "uk_product_name") // Ensure product names are unique
	    }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use database auto-increment
    private Long id;

    @Column(nullable = false, length = 150) // Product name is required
    private String name;

    @Column(length = 50) // Price description string
    private String price;

    @Column(length = 512) // URL for the image
    private String image;

    @Column(nullable = false, length = 100) // Supplier name is required
    private String supplierName; // Ensure casing matches index/columnList

    // Optional: Add more fields as needed
    @Column(columnDefinition = "TEXT") // Longer text description
    private String description;

    @Column(length = 100)
    private String category;

    // Optional: Timestamps managed by Hibernate
    @CreationTimestamp // Automatically set on creation
    @Column(updatable = false) // Prevent updates to creation timestamp
    private Instant createdAt;

    @UpdateTimestamp // Automatically set on creation and update
    private Instant updatedAt;

}