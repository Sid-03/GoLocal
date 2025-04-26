package com.golocal.inquiryservice.controller;

import com.golocal.inquiryservice.dto.InquiryRequestDto;
import com.golocal.inquiryservice.dto.InquiryResponseDto;
import com.golocal.inquiryservice.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling inquiry-related requests.
 */
@RestController
@RequestMapping("/api/inquiries") // Base path for inquiry endpoints
@RequiredArgsConstructor
@Slf4j
// No @CrossOrigin if handled globally
public class InquiryController {

    private final InquiryService inquiryService;
    private static final String USER_ID_HEADER = "X-User-Id"; // Consistent header name

    /**
     * POST /api/inquiries : Create a new inquiry.
     * Requires authentication (verified by Gateway).
     * Extracts user ID from the X-User-Id header added by the gateway.
     *
     * @param inquiryRequestDto DTO containing inquiry details.
     * @param userId            The user ID extracted from the request header.
     * @return ResponseEntity containing the created InquiryResponseDto (HTTP 201 Created).
     */
    @PostMapping
    // @PreAuthorize("isAuthenticated()") // Alternative: Method-level security check
    public ResponseEntity<InquiryResponseDto> createInquiry(
            @Valid @RequestBody InquiryRequestDto inquiryRequestDto,
            @RequestHeader(name = USER_ID_HEADER, required = true) String userId) { // Get userId from header

        log.info("Received request to create inquiry from User ID: {} for Product ID: {}", userId, inquiryRequestDto.getProductId());

         // Optional: Basic check if header is present (though required=true handles it)
         if (!StringUtils.hasText(userId)) {
             log.warn("Missing or empty {} header in request.", USER_ID_HEADER);
             // Consider returning a 400 Bad Request if the header is expected but missing/empty
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or a proper error DTO
         }


        InquiryResponseDto createdInquiry = inquiryService.createInquiry(inquiryRequestDto, userId);
        log.info("Inquiry created successfully with ID: {}", createdInquiry.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInquiry);
    }

    /**
     * GET /api/inquiries/my-inquiries : Get all inquiries submitted by the currently authenticated user.
     * Requires authentication. Extracts user ID from the X-User-Id header.
     *
     * @param userId The user ID extracted from the request header.
     * @return ResponseEntity containing a list of InquiryResponseDtos (HTTP 200 OK).
     */
    @GetMapping("/my-inquiries")
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<InquiryResponseDto>> getMyInquiries(
            @RequestHeader(name = USER_ID_HEADER, required = true) String userId) {

        log.info("Received request to get inquiries for User ID: {}", userId);

         if (!StringUtils.hasText(userId)) {
             log.warn("Missing or empty {} header in request.", USER_ID_HEADER);
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
         }

        List<InquiryResponseDto> inquiries = inquiryService.getInquiriesForUser(userId);
        log.debug("Returning {} inquiries for user ID: {}", inquiries.size(), userId);
        return ResponseEntity.ok(inquiries);
    }

    // --- Alternative way to get User ID using Principal (requires more config) ---
    /*
    @PostMapping("/alt")
    public ResponseEntity<InquiryResponseDto> createInquiryPrincipal(
            @Valid @RequestBody InquiryRequestDto inquiryRequestDto,
            Principal principal) { // Inject Principal

        if (principal == null || principal.getName() == null) {
             log.warn("Cannot create inquiry: User principal not found in security context.");
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Or Forbidden
        }
        String username = principal.getName(); // Typically username
        // You might need another service call here to get the actual User ID based on username
        // String userId = userServiceClient.getUserIdByUsername(username); // Example
         String userId = username; // Using username as ID for simplicity here

        log.info("Received request to create inquiry from Principal: {} for Product ID: {}", userId, inquiryRequestDto.getProductId());
        InquiryResponseDto createdInquiry = inquiryService.createInquiry(inquiryRequestDto, userId);
        log.info("Inquiry created successfully with ID: {}", createdInquiry.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInquiry);
    }
    */

    // Add other endpoints as needed (e.g., GET /api/inquiries/{id})
    // Ensure proper authorization checks if implementing endpoints for suppliers/admins.
    /*
     @GetMapping("/{id}")
     public ResponseEntity<InquiryResponseDto> getInquiryById(
             @PathVariable Long id,
             @RequestHeader(name = USER_ID_HEADER) String userId) {
         log.info("Request to get inquiry ID {} by user {}", id, userId);
         // Fetch inquiry and verify if the current user (userId) is allowed to see it
         // Inquiry inquiry = inquiryService.getInquiryByIdAndValidateUser(id, userId); // Example service method
         // return ResponseEntity.ok(mapToDto(inquiry));
         return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
     }
    */
}