package com.golocal.inquiryservice.repository;

import com.golocal.inquiryservice.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Inquiry entity.
 */
@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> { // Entity is Inquiry, ID is Long

    /**
     * Finds all inquiries submitted by a specific user, ordered by creation date descending.
     *
     * @param userId The ID of the user whose inquiries are to be fetched.
     *               (Type should match the userId field in the Inquiry entity - String or Long).
     * @return A list of Inquiry objects submitted by the user. Returns an empty list if none found.
     */
    List<Inquiry> findByUserIdOrderByCreatedAtDesc(String userId); // Assuming userId is String

    /**
     * Finds all inquiries related to a specific product, ordered by creation date descending.
     * (Useful for suppliers viewing inquiries about their products, if implementing that).
     *
     * @param productId The ID of the product.
     * @return A list of Inquiry objects related to the product.
     */
    List<Inquiry> findByProductIdOrderByCreatedAtDesc(Long productId);

    // Add other query methods as needed, e.g., finding by status, supplier, etc.
    // List<Inquiry> findBySupplierNameAndStatus(String supplierName, String status);
}