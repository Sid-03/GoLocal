package com.golocal.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing Product information.
 * Used for transferring product data between layers (e.g., service to controller)
 * and in API responses.
 */
@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id; // The unique identifier for the product

    private String name; // The name of the product

    private String price; // The price description (e.g., "$ 2.00 per lb")

    private String image; // URL or path to the product image

    private String supplierName; // Name of the supplier providing the product

    // Add other relevant fields if needed, e.g., description, category, stock quantity
    // private String description;
    // private String category;
    // private Integer stockQuantity;

    // Consider adding timestamps (createdAt, updatedAt) if relevant for the API consumer
    // private java.time.Instant createdAt;
    // private java.time.Instant updatedAt;
}