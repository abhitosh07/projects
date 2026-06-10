package com.example.crud.repository;

import com.example.crud.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * JPA Repository for Product CRUD operations.
 * Extends JpaRepository to get built-in CRUD methods:
 *   - save()       → Create / Update
 *   - findById()   → Read by ID
 *   - findAll()    → Read all
 *   - deleteById() → Delete by ID
 *   - existsById() → Check existence
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find all products by category (case-insensitive).
     */
    List<Product> findByCategoryIgnoreCase(String category);

    /**
     * Find products whose name contains the given keyword (case-insensitive).
     */
    List<Product> findByNameContainingIgnoreCase(String keyword);

    /**
     * Find products within a price range using JPQL.
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Check if a product with the given name already exists.
     */
    boolean existsByNameIgnoreCase(String name);
}
