package com.golocal.productservice.repository;

import com.golocal.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> { // Entity is Product, ID is Long

    /**
     * Finds products whose name OR supplier name contains the given search term (case-insensitive).
     * Used for implementing the product search functionality.
     *
     * @param nameSearchTerm The term to search for in the product name.
     * @param supplierSearchTerm The term to search for in the supplier name.
     * @return A list of matching Products. Returns an empty list if no matches are found.
     */
    List<Product> findByNameContainingIgnoreCaseOrSupplierNameContainingIgnoreCase(String nameSearchTerm, String supplierSearchTerm);

    /**
     * Finds products whose name contains the given search term (case-insensitive).
     * Alternative search method focusing only on product name.
     *
     * @param nameSearchTerm The term to search for in the product name.
     * @return A list of matching Products.
     */
    List<Product> findByNameContainingIgnoreCase(String nameSearchTerm);

    // Add more specific query methods if needed, e.g.:
    // List<Product> findByCategoryIgnoreCase(String category);
    // List<Product> findBySupplierNameIgnoreCase(String supplierName);

}