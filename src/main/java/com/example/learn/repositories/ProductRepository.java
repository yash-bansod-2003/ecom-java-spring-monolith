package com.example.learn.repositories;

import com.example.learn.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find product by name
     */
    Optional<Product> findByName(String name);

    /**
     * Find product by SKU
     */
    Optional<Product> findBySku(String sku);

    /**
     * Find products by category
     */
    List<Product> findByCategoryIgnoreCase(String category);

    /**
     * Find all active products
     */
    List<Product> findByIsActiveTrue();

    /**
     * Find products with quantity greater than specified value
     */
    List<Product> findByQuantityGreaterThan(Integer quantity);

    /**
     * Find in-stock products (quantity > 0)
     */
    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND p.isActive = true")
    List<Product> findInStockProducts();

    /**
     * Find out-of-stock products (quantity = 0)
     */
    @Query("SELECT p FROM Product p WHERE p.quantity = 0 AND p.isActive = true")
    List<Product> findOutOfStockProducts();

    /**
     * Find products within price range
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Search products by name containing keyword
     */
    List<Product> findByNameContainingIgnoreCase(String keyword);

    /**
     * Check if product exists by name
     */
    boolean existsByName(String name);

    /**
     * Check if product exists by SKU
     */
    boolean existsBySku(String sku);

    /**
     * Count active products
     */
    long countByIsActiveTrue();

    /**
     * Count products by category
     */
    long countByCategory(String category);
}

