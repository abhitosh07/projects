package com.example.crud.service;

import com.example.crud.dto.ProductRequestDto;
import com.example.crud.dto.ProductResponseDto;
import com.example.crud.entity.Product;
import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService.
 * Handles all business logic and delegates persistence to ProductRepository (JPA).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // ─────────────────────────────────────────
    //  CREATE
    // ─────────────────────────────────────────

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        log.info("Creating new product with name: {}", requestDto.getName());

        // Check for duplicate name
        if (productRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException(
                    "Product with name '" + requestDto.getName() + "' already exists.");
        }

        Product product = mapToEntity(requestDto);
        Product savedProduct = productRepository.save(product);

        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return mapToResponseDto(savedProduct);
    }

    // ─────────────────────────────────────────
    //  READ ALL
    // ─────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    //  READ BY ID
    // ─────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = findProductByIdOrThrow(id);
        return mapToResponseDto(product);
    }

    // ─────────────────────────────────────────
    //  UPDATE
    // ─────────────────────────────────────────

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        log.info("Updating product with ID: {}", id);

        Product existingProduct = findProductByIdOrThrow(id);

        // If name changed, check for duplicate
        if (!existingProduct.getName().equalsIgnoreCase(requestDto.getName())
                && productRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException(
                    "Product with name '" + requestDto.getName() + "' already exists.");
        }

        existingProduct.setName(requestDto.getName());
        existingProduct.setDescription(requestDto.getDescription());
        existingProduct.setPrice(requestDto.getPrice());
        existingProduct.setQuantity(requestDto.getQuantity());
        existingProduct.setCategory(requestDto.getCategory());

        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        return mapToResponseDto(updatedProduct);
    }

    // ─────────────────────────────────────────
    //  DELETE
    // ─────────────────────────────────────────

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        findProductByIdOrThrow(id); // ensures it exists
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    // ─────────────────────────────────────────
    //  SEARCH / FILTER
    // ─────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsByName(String keyword) {
        log.info("Searching products by keyword: {}", keyword);
        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Fetching products in price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceRange(minPrice, maxPrice)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    //  HELPER METHODS
    // ─────────────────────────────────────────

    /**
     * Finds a product by ID or throws ResourceNotFoundException.
     */
    private Product findProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    /**
     * Maps a ProductRequestDto to a Product entity.
     */
    private Product mapToEntity(ProductRequestDto dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .category(dto.getCategory())
                .build();
    }

    /**
     * Maps a Product entity to a ProductResponseDto.
     */
    private ProductResponseDto mapToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
