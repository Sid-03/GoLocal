package com.golocal.productservice.service;

import com.golocal.productservice.dto.ProductDto;
import com.golocal.productservice.entity.Product;
import com.golocal.productservice.exception.ProductNotFoundException;
import com.golocal.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // For checking blank search term

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing Product data.
 * Contains business logic related to products.
 */
@Service
@RequiredArgsConstructor // Lombok constructor injection
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Retrieves a list of all products, optionally filtered by a search term.
     * If a search term is provided, it searches in product name and supplier name.
     *
     * @param searchTerm Optional search term (can be null or empty).
     * @return A list of ProductDto objects.
     */
    @Transactional(readOnly = true) // Read-only transaction for fetching data
    public List<ProductDto> getAllProducts(String searchTerm) {
        List<Product> products;

        // Check if the search term is provided and not blank
        if (StringUtils.hasText(searchTerm)) {
            log.debug("Searching products with term: '{}'", searchTerm);
            // Use the repository method for combined name/supplier search
            products = productRepository.findByNameContainingIgnoreCaseOrSupplierNameContainingIgnoreCase(searchTerm, searchTerm);
        } else {
            log.debug("Fetching all products (no search term).");
            // Fetch all products if no search term is given
            products = productRepository.findAll();
        }

        // Map the list of Product entities to a list of ProductDto objects
        return products.stream()
                .map(this::mapToProductDto) // Use helper method for mapping
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The ProductDto object for the found product.
     * @throws ProductNotFoundException if no product exists with the given ID.
     */
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        log.debug("Fetching product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ProductNotFoundException(id); // Throw specific exception
                });
        log.debug("Product found: {}", product.getName());
        return mapToProductDto(product); // Map the found entity to DTO
    }

    // --- Add methods for CREATE, UPDATE, DELETE if needed ---
    /*
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
         log.info("Creating new product: {}", productDto.getName());
         Product product = mapToProductEntity(productDto); // Map DTO to Entity
         // Clear ID for creation
         product.setId(null);
         // Clear timestamps, they will be set automatically
         product.setCreatedAt(null);
         product.setUpdatedAt(null);
         Product savedProduct = productRepository.save(product);
         log.info("Product created with ID: {}", savedProduct.getId());
         return mapToProductDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        log.info("Updating product with ID: {}", id);
        Product existingProduct = productRepository.findById(id)
             .orElseThrow(() -> new ProductNotFoundException(id));

        // Update fields from DTO (add null checks if necessary)
        existingProduct.setName(productDto.getName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setImage(productDto.getImage());
        existingProduct.setSupplierName(productDto.getSupplierName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setCategory(productDto.getCategory());
        // Note: createdAt is usually not updated. updatedAt will be set automatically.

        Product updatedProduct = productRepository.save(existingProduct);
         log.info("Product updated successfully: ID {}", id);
        return mapToProductDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
         log.info("Deleting product with ID: {}", id);
         if (!productRepository.existsById(id)) {
             throw new ProductNotFoundException(id);
         }
         productRepository.deleteById(id);
         log.info("Product deleted successfully: ID {}", id);
    }
    */

    // --- Helper Methods for Mapping ---

    /**
     * Maps a Product entity to a ProductDto object.
     *
     * @param product The Product entity.
     * @return The corresponding ProductDto object.
     */
    private ProductDto mapToProductDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImage(),
                product.getSupplierName()
                // Map other fields if they exist in DTO
                // product.getDescription(),
                // product.getCategory(),
                // product.getCreatedAt(),
                // product.getUpdatedAt()
        );
    }

}