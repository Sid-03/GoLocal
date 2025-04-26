package com.golocal.inquiryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * JPA Entity representing a product inquiry sent by a user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inquiry", // Table name in the database
        indexes = {
                @Index(name = "idx_inquiry_user", columnList = "userId"),      // Index for fetching by user
                @Index(name = "idx_inquiry_product", columnList = "productId"), // Index for fetching by product
                @Index(name = "idx_inquiry_created", columnList = "createdAt") // Index on timestamp
        })
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;

    @Column(nullable = false) // ID of the product being inquired about
    private Long productId;

    /**
     * The ID of the user who submitted the inquiry.
     * This typically comes from the authenticated user context (e.g., 'X-User-Id' header).
     * Storing it as a String might be suitable if user IDs are UUIDs or come from an external system.
     * If user IDs are numeric Longs from the User service DB, use Long type.
     */
    @Column(nullable = false, length = 100) // Adjust length/type based on user ID format
    private String userId;

    /**
     * Denormalized supplier name for easier retrieval/display.
     * Alternatively, this could be fetched from the Product service when needed,
     * but storing it here reduces inter-service calls for simple listing.
     */
    @Column(length = 100)
    private String supplierName; // Consider fetching this based on productId if not storing

    @Column(nullable = false, length = 255) // Inquiry subject line
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT") // Inquiry message body
    private String message;

    // Timestamps managed by Hibernate
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    // Optional: Add fields for status tracking, replies, etc.
    // @Column(length = 50)
    // private String status = "NEW";
    //
    // @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Reply> replies;

}