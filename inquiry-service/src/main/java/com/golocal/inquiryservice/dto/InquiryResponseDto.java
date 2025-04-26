package com.golocal.inquiryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Data Transfer Object (DTO) representing an inquiry in API responses.
 * Used when fetching existing inquiries.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponseDto {

    private Long id; // The unique ID of the inquiry

    private Long productId; // ID of the product inquired about

    private String userId; // ID of the user who sent the inquiry

    // Optional: Include product name/supplier name for convenience in the UI
    // These might be denormalized in the Inquiry entity or fetched separately.
    // private String productName;
     private String supplierName; // Assuming this is stored/retrieved

    private String subject; // The subject of the inquiry

    private String message; // The message content

    private Instant createdAt; // Timestamp when the inquiry was created

    private Instant updatedAt; // Timestamp when the inquiry was last updated (e.g., if replies are added)

    // Optional: Fields for replies/status if implementing a more complex chat feature
    // private String status; // e.g., "NEW", "READ", "REPLIED"
    // private List<ReplyDto> replies;

}