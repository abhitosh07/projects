package com.example.crud.service;

import com.example.crud.dto.ProductRequestDto;
import com.example.crud.dto.ProductResponseDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface defining the contract for Product business operations.
 */
public interface ProductService {

    /**
     * Create a new product.
     *
     * @param requestDto product data
     * @return created product response
     */
    ProductResponseDto createProduct(ProductRequestDto requestDto);

    /**
     * Retrieve all products.
     *
     * @return list of all products
     */
    List<ProductResponseDto> getAllProducts();

    /**
     * Retrieve a product by its ID.
     *
     * @param id product ID
     * @return product response
     */
    ProductResponseDto getProductById(Long id);

    /**
     * Update an existing product.
     *
     * @param id         product ID
     * @param requestDto updated product data
     * @return updated product response
     */
    ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);

    /**
     * Delete a product by its ID.
     *
     * @param id product ID
     */
    void deleteProduct(Long id);

    /**
     * Find products by category.
     *
     * @param category category name
     * @return list of products in the category
     */
    List<ProductResponseDto> getProductsByCategory(String category);

    /**
     * Search products by name keyword.
     *
     * @param keyword search keyword
     * @return list of matching products
     */
    List<ProductResponseDto> searchProductsByName(String keyword);

    /**
     * Find products within a price range.
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of products in the price range
     */
    List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
}
