package com.golocal.productservice.controller;

import com.golocal.productservice.dto.ProductDto;
import com.golocal.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling product-related requests.
 */
@RestController
@RequestMapping("/api/products") // Base path for product endpoints
@RequiredArgsConstructor // Lombok constructor injection
@Slf4j
// No @CrossOrigin here if CORS is handled globally by CorsConfig or Gateway
public class ProductController {

    private final ProductService productService; // Inject the service

    /**
     * GET /api/products : Get all products, optionally filtered by a search term.
     *
     * @param search Optional search term provided as a query parameter.
     * @return ResponseEntity containing a list of ProductDtos (HTTP 200 OK).
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(value = "search", required = false) String search) {

        if (search != null && !search.isBlank()) {
            log.info("Received request to get products with search term: '{}'", search);
        } else {
            log.info("Received request to get all products");
        }

        List<ProductDto> products = productService.getAllProducts(search);
        log.debug("Returning {} products.", products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id} : Get a specific product by its ID.
     *
     * @param id The ID of the product to retrieve (path variable).
     * @return ResponseEntity containing the ProductDto (HTTP 200 OK) if found,
     *         or triggers the GlobalExceptionHandler for 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        log.info("Received request to get product by ID: {}", id);
        ProductDto product = productService.getProductById(id);
        // If service throws ProductNotFoundException, GlobalExceptionHandler handles it
        log.debug("Returning product: {}", product.getName());
        return ResponseEntity.ok(product);
    }

    // --- Add endpoints for CREATE, UPDATE, DELETE if needed ---
    /*
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Received request to create product: {}", productDto.getName());
        ProductDto createdProduct = productService.createProduct(productDto);
        // Return 201 Created status with the created product and location header
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        log.info("Received request to update product ID: {}", id);
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
         log.info("Received request to delete product ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content on success
    }
    */
}