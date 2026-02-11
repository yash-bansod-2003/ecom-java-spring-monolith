package com.example.learn.controllers;

import com.example.learn.dto.ApiResponse;
import com.example.learn.dto.ProductRequest;
import com.example.learn.dto.ProductResponse;
import com.example.learn.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Get all products
     * @return List of all products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.findAll();
        return ResponseEntity.ok(
            ApiResponse.success("Products retrieved successfully", products)
        );
    }

    /**
     * Get product by ID
     * @param id Product ID
     * @return Product details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(
            ApiResponse.success("Product retrieved successfully", product)
        );
    }

    /**
     * Get product by name
     * @param name Product name
     * @return Product details
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductByName(@PathVariable String name) {
        ProductResponse product = productService.findByName(name);
        return ResponseEntity.ok(
            ApiResponse.success("Product retrieved successfully", product)
        );
    }

    /**
     * Get product by SKU
     * @param sku Product SKU
     * @return Product details
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySku(@PathVariable String sku) {
        ProductResponse product = productService.findBySku(sku);
        return ResponseEntity.ok(
            ApiResponse.success("Product retrieved successfully", product)
        );
    }

    /**
     * Get products by category
     * @param category Product category
     * @return List of products in category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponse> products = productService.findByCategory(category);
        return ResponseEntity.ok(
            ApiResponse.success("Products retrieved successfully", products)
        );
    }

    /**
     * Get all active products
     * @return List of active products
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        List<ProductResponse> products = productService.findActiveProducts();
        return ResponseEntity.ok(
            ApiResponse.success("Active products retrieved successfully", products)
        );
    }

    /**
     * Get in-stock products
     * @return List of in-stock products
     */
    @GetMapping("/in-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getInStockProducts() {
        List<ProductResponse> products = productService.findInStockProducts();
        return ResponseEntity.ok(
            ApiResponse.success("In-stock products retrieved successfully", products)
        );
    }

    /**
     * Get out-of-stock products
     * @return List of out-of-stock products
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getOutOfStockProducts() {
        List<ProductResponse> products = productService.findOutOfStockProducts();
        return ResponseEntity.ok(
            ApiResponse.success("Out-of-stock products retrieved successfully", products)
        );
    }

    /**
     * Get products within price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of products
     */
    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ProductResponse> products = productService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(
            ApiResponse.success("Products retrieved successfully", products)
        );
    }

    /**
     * Search products by name
     * @param keyword Search keyword
     * @return List of matching products
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @RequestParam(name = "keyword") String keyword) {
        List<ProductResponse> products = productService.searchByName(keyword);
        return ResponseEntity.ok(
            ApiResponse.success("Search completed successfully", products)
        );
    }

    /**
     * Create new product
     * @param productRequest Product data
     * @return Created product
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("Product created successfully", createdProduct)
        );
    }

    /**
     * Update existing product
     * @param id Product ID
     * @param productRequest Updated product data
     * @return Updated product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(
            ApiResponse.success("Product updated successfully", updatedProduct)
        );
    }

    /**
     * Delete product
     * @param id Product ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
            ApiResponse.success("Product deleted successfully", null)
        );
    }

    /**
     * Deactivate product (soft delete)
     * @param id Product ID
     * @return Deactivated product
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(@PathVariable Long id) {
        ProductResponse product = productService.deactivateProduct(id);
        return ResponseEntity.ok(
            ApiResponse.success("Product deactivated successfully", product)
        );
    }

    /**
     * Activate product
     * @param id Product ID
     * @return Activated product
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<ProductResponse>> activateProduct(@PathVariable Long id) {
        ProductResponse product = productService.activateProduct(id);
        return ResponseEntity.ok(
            ApiResponse.success("Product activated successfully", product)
        );
    }

    /**
     * Update product quantity
     * @param id Product ID
     * @param quantity New quantity
     * @return Updated product
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        ProductResponse product = productService.updateQuantity(id, quantity);
        return ResponseEntity.ok(
            ApiResponse.success("Product quantity updated successfully", product)
        );
    }

    /**
     * Get total product count
     * @return Count of products
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getProductCount() {
        long count = productService.count();
        return ResponseEntity.ok(
            ApiResponse.success("Product count retrieved successfully", count)
        );
    }

    /**
     * Get active product count
     * @return Count of active products
     */
    @GetMapping("/count/active")
    public ResponseEntity<ApiResponse<Long>> getActiveProductCount() {
        long count = productService.countActive();
        return ResponseEntity.ok(
            ApiResponse.success("Active product count retrieved successfully", count)
        );
    }

    /**
     * Get product count by category
     * @param category Product category
     * @return Count of products in category
     */
    @GetMapping("/count/category/{category}")
    public ResponseEntity<ApiResponse<Long>> getProductCountByCategory(@PathVariable String category) {
        long count = productService.countByCategory(category);
        return ResponseEntity.ok(
            ApiResponse.success("Product count by category retrieved successfully", count)
        );
    }
}

