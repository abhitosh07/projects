package com.example.crud;

import com.example.crud.entity.Product;
import com.example.crud.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds the H2 database with sample data on application startup.
 * Useful for quick testing via H2 console or REST API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            log.info("Seeding sample product data...");

            List<Product> products = List.of(
                    Product.builder()
                            .name("MacBook Pro 14\"")
                            .description("Apple M3 Pro chip, 18GB RAM, 512GB SSD")
                            .price(new BigDecimal("1999.99"))
                            .quantity(25)
                            .category("Electronics")
                            .build(),
                    Product.builder()
                            .name("Sony WH-1000XM5")
                            .description("Industry-leading noise canceling wireless headphones")
                            .price(new BigDecimal("349.99"))
                            .quantity(50)
                            .category("Electronics")
                            .build(),
                    Product.builder()
                            .name("Ergonomic Office Chair")
                            .description("Lumbar support, adjustable armrests, breathable mesh")
                            .price(new BigDecimal("459.00"))
                            .quantity(15)
                            .category("Furniture")
                            .build(),
                    Product.builder()
                            .name("The Pragmatic Programmer")
                            .description("Classic software engineering book by David Thomas & Andrew Hunt")
                            .price(new BigDecimal("49.95"))
                            .quantity(100)
                            .category("Books")
                            .build(),
                    Product.builder()
                            .name("Standing Desk 60\"")
                            .description("Electric height-adjustable standing desk with memory presets")
                            .price(new BigDecimal("799.00"))
                            .quantity(10)
                            .category("Furniture")
                            .build()
            );

            productRepository.saveAll(products);
            log.info("Successfully seeded {} products.", products.size());
        }
    }
}
