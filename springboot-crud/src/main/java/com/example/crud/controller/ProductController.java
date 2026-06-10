package com.example.crud.controller;

import com.example.crud.dto.ProductRequestDto;
import com.example.crud.dto.ProductResponseDto;
import com.example.crud.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller exposing CRUD endpoints for Products.
 *
 * Base URL: /api/products
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ─────────────────────────────────────────
    //  GET /api/products/ping
    //  Check if the application is running
    // ─────────────────────────────────────────

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    // ─────────────────────────────────────────
    //  POST /api/products
    //  Create a new product
    // ─────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody ProductRequestDto requestDto) {

        ProductResponseDto createdProduct = productService.createProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // ─────────────────────────────────────────
    //  GET /api/products
    //  Get all products (with optional filters)
    // ─────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(productService.searchProductsByName(search));
        }
        if (minPrice != null && maxPrice != null) {
            return ResponseEntity.ok(productService.getProductsByPriceRange(minPrice, maxPrice));
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ─────────────────────────────────────────
    //  GET /api/products/{id}
    //  Get a product by ID
    // ─────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ─────────────────────────────────────────
    //  PUT /api/products/{id}
    //  Update an existing product
    // ─────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto requestDto) {

        ProductResponseDto updatedProduct = productService.updateProduct(id, requestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    // ─────────────────────────────────────────
    //  DELETE /api/products/{id}
    //  Delete a product
    // ─────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
