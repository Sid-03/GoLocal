package com.golocal.inquiryservice.service;

import com.golocal.inquiryservice.dto.InquiryRequestDto;
import com.golocal.inquiryservice.dto.InquiryResponseDto;
import com.golocal.inquiryservice.entity.Inquiry;
import com.golocal.inquiryservice.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// If fetching product/supplier info is needed:
// import org.springframework.web.client.RestTemplate; // or WebClient for reactive
// import org.springframework.beans.factory.annotation.Value;
// import com.golocal.inquiryservice.dto.ProductDto; // Need a DTO to deserialize product info

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for handling inquiry business logic.
 * Responsible for creating and retrieving inquiries.
 */
@Service
@RequiredArgsConstructor // Lombok constructor injection for final fields
@Slf4j // Lombok logger
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    // Optional: For fetching data from Product Service if needed
    // Make sure to configure the RestTemplate/WebClient Bean if you use this
    // private final RestTemplate restTemplate;
    // @Value("${product.service.url}") // Example: Define this in application.properties if needed
    // private String productServiceBaseUrl;

    /**
     * Creates and saves a new inquiry based on the request data and user ID.
     *
     * @param requestDto The DTO containing inquiry details (productId, subject, message).
     * @param userId     The ID of the user submitting the inquiry (obtained from security context/header).
     * @return InquiryResponseDto representing the newly created inquiry.
     */
    @Transactional // Mark method as transactional (modifies data)
    public InquiryResponseDto createInquiry(InquiryRequestDto requestDto, String userId) {
        log.info("Attempting to create inquiry for product ID {} by user ID {}", requestDto.getProductId(), userId);

        // Create a new Inquiry entity from the DTO
        Inquiry inquiry = new Inquiry();
        inquiry.setProductId(requestDto.getProductId());
        inquiry.setSubject(requestDto.getSubject());
        inquiry.setMessage(requestDto.getMessage());
        inquiry.setUserId(userId); // Set the user ID obtained from the authenticated context

        // --- Optional: Fetch Supplier Name from Product Service ---
        // If you choose to do this, uncomment the RestTemplate/WebClient dependencies and configuration.
        // This adds complexity and inter-service dependency. Storing supplier name during inquiry
        // creation might be simpler if the frontend already has it, or just store Product ID.
        String supplierName = "Supplier Placeholder"; // Default/placeholder
        /*
        try {
            String productApiUrl = productServiceBaseUrl + "/api/products/" + requestDto.getProductId();
            log.debug("Fetching product details from URL: {}", productApiUrl);
            // Define a DTO matching the Product Service response if using RestTemplate
            // ResponseEntity<ProductDto> response = restTemplate.getForEntity(productApiUrl, ProductDto.class);
            // if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            //     supplierName = response.getBody().getSupplierName();
            //     log.debug("Fetched supplier name: {}", supplierName);
            // } else {
            //     log.warn("Could not retrieve product details (Status: {}) for product ID {}", response.getStatusCode(), requestDto.getProductId());
            // }
        } catch (Exception e) {
            log.error("Error fetching product details for ID {}: {}", requestDto.getProductId(), e.getMessage(), e);
            supplierName = "Error Fetching Supplier"; // Indicate error fetching info
        }
        */
        inquiry.setSupplierName(supplierName); // Set the fetched or placeholder name


        // Save the inquiry entity to the database using the repository
        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        log.info("Inquiry created successfully with ID: {} for user ID: {}", savedInquiry.getId(), userId);

        // Map the saved entity back to a response DTO to return
        return mapToInquiryResponseDto(savedInquiry);
    }

    /**
     * Retrieves all inquiries submitted by a specific user.
     *
     * @param userId The ID of the user whose inquiries are to be fetched.
     * @return A list of InquiryResponseDto objects, ordered by creation date descending.
     */
    @Transactional(readOnly = true) // Read-only transaction
    public List<InquiryResponseDto> getInquiriesForUser(String userId) {
        log.debug("Fetching inquiries for user ID: {}", userId);
        // Use the repository method to find inquiries by userId
        List<Inquiry> inquiries = inquiryRepository.findByUserIdOrderByCreatedAtDesc(userId);
        log.debug("Found {} inquiries for user ID: {}", inquiries.size(), userId);

        // Map the list of Inquiry entities to a list of InquiryResponseDto objects
        return inquiries.stream()
                .map(this::mapToInquiryResponseDto) // Use the helper method for mapping
                .collect(Collectors.toList());
    }

    // === Add other service methods as needed ===
    // E.g., getInquiryById, getInquiriesByProduct, updateInquiryStatus, deleteInquiry

    /*
    @Transactional(readOnly = true)
    public InquiryResponseDto getInquiryByIdAndValidateUser(Long inquiryId, String userId) {
        log.debug("Fetching inquiry ID {} for user ID {}", inquiryId, userId);
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
             .orElseThrow(() -> new RuntimeException("Inquiry not found with ID: " + inquiryId)); // Replace with specific exception

        // Authorization check: Ensure the inquiry belongs to the requesting user
        if (!inquiry.getUserId().equals(userId)) {
             log.warn("Access denied: User {} attempted to access inquiry ID {} belonging to user {}", userId, inquiryId, inquiry.getUserId());
             throw new org.springframework.security.access.AccessDeniedException("You do not have permission to view this inquiry.");
        }
        return mapToInquiryResponseDto(inquiry);
    }
    */


    // --- Helper Method for Mapping Entity to DTO ---

    /**
     * Maps an Inquiry entity object to an InquiryResponseDto object.
     *
     * @param inquiry The Inquiry entity from the database.
     * @return The corresponding InquiryResponseDto for API responses.
     */
    private InquiryResponseDto mapToInquiryResponseDto(Inquiry inquiry) {
        if (inquiry == null) {
            return null;
        }
        // Create and populate the DTO
        return new InquiryResponseDto(
                inquiry.getId(),
                inquiry.getProductId(),
                inquiry.getUserId(),
                inquiry.getSupplierName(), // Include supplier name
                inquiry.getSubject(),
                inquiry.getMessage(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
                // Map other fields like status or replies if they were added to the entity/DTO
        );
    }
}