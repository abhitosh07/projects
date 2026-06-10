package com.example.crud;

import com.example.crud.dto.ProductRequestDto;
import com.example.crud.dto.ProductResponseDto;
import com.example.crud.entity.Product;
import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.repository.ProductRepository;
import com.example.crud.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductServiceImpl using Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;
    private ProductRequestDto sampleRequest;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("A test product")
                .price(new BigDecimal("99.99"))
                .quantity(10)
                .category("Electronics")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sampleRequest = ProductRequestDto.builder()
                .name("Test Product")
                .description("A test product")
                .price(new BigDecimal("99.99"))
                .quantity(10)
                .category("Electronics")
                .build();
    }

    // ── CREATE ────────────────────────────────

    @Test
    @DisplayName("Should create a product successfully")
    void shouldCreateProductSuccessfully() {
        when(productRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponseDto result = productService.createProduct(sampleRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when product name already exists")
    void shouldThrowExceptionWhenDuplicateName() {
        when(productRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(sampleRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(productRepository, never()).save(any());
    }

    // ── READ ──────────────────────────────────

    @Test
    @DisplayName("Should return all products")
    void shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> results = productService.getAllProducts();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Should return product by ID")
    void shouldReturnProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponseDto result = productService.getProductById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product not found by ID")
    void shouldThrowNotFoundWhenProductIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    // ── UPDATE ────────────────────────────────

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductSuccessfully() {
        ProductRequestDto updateRequest = ProductRequestDto.builder()
                .name("Updated Product")
                .description("Updated desc")
                .price(new BigDecimal("149.99"))
                .quantity(20)
                .category("Electronics")
                .build();

        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Updated Product")
                .description("Updated desc")
                .price(new BigDecimal("149.99"))
                .quantity(20)
                .category("Electronics")
                .createdAt(sampleProduct.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.existsByNameIgnoreCase("Updated Product")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponseDto result = productService.updateProduct(1L, updateRequest);

        assertThat(result.getName()).isEqualTo("Updated Product");
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("149.99"));
    }

    // ── DELETE ────────────────────────────────

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProductSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).deleteById(1L);

        assertThatNoException().isThrownBy(() -> productService.deleteProduct(1L));

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent product")
    void shouldThrowNotFoundWhenDeletingNonExistentProduct() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, never()).deleteById(any());
    }
}
