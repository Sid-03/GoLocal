package com.golocal.inquiryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the request body for creating a new inquiry.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequestDto {

    /**
     * The ID of the product the inquiry is about.
     * Must not be null.
     */
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    /**
     * The subject line for the inquiry.
     * Must not be blank and should have a reasonable size.
     */
    @NotBlank(message = "Subject cannot be blank")
    @Size(min = 3, max = 200, message = "Subject must be between 3 and 200 characters")
    private String subject;

    /**
     * The main message content of the inquiry.
     * Must not be blank.
     */
    @NotBlank(message = "Message cannot be blank")
    @Size(max = 5000, message = "Message cannot exceed 5000 characters")
    private String message;

    // Note: userId (sender) is typically not included in the request DTO.
    // It should be obtained from the security context or the 'X-User-Id' header
    // added by the API Gateway after validating the JWT token.

    // Optional: If needed, you could include supplier info if the frontend knows it,
    // but often the backend might look this up based on productId if required.
    // private String supplierName;
}