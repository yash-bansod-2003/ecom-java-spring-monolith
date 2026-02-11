package com.example.learn.services;

import com.example.learn.dto.ProductRequest;
import com.example.learn.dto.ProductResponse;
import com.example.learn.exceptions.DuplicateResourceException;
import com.example.learn.exceptions.ResourceNotFoundException;
import com.example.learn.mappers.ProductMapper;
import com.example.learn.models.Product;
import com.example.learn.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Get all products
     * @return List of ProductResponse
     */
    public List<ProductResponse> findAll() {
        log.debug("Fetching all products");
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get product by ID
     * @param id Product ID
     * @return ProductResponse
     * @throws ResourceNotFoundException if product not found
     */
    public ProductResponse findById(Long id) {
        log.debug("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toResponse(product);
    }

    /**
     * Get product by name
     * @param name Product name
     * @return ProductResponse
     * @throws ResourceNotFoundException if product not found
     */
    public ProductResponse findByName(String name) {
        log.debug("Fetching product with name: {}", name);
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "name", name));
        return productMapper.toResponse(product);
    }

    /**
     * Get product by SKU
     * @param sku Product SKU
     * @return ProductResponse
     * @throws ResourceNotFoundException if product not found
     */
    public ProductResponse findBySku(String sku) {
        log.debug("Fetching product with SKU: {}", sku);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "sku", sku));
        return productMapper.toResponse(product);
    }

    /**
     * Get products by category
     * @param category Product category
     * @return List of ProductResponse
     */
    public List<ProductResponse> findByCategory(String category) {
        log.debug("Fetching products with category: {}", category);
        return productRepository.findByCategoryIgnoreCase(category).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all active products
     * @return List of ProductResponse
     */
    public List<ProductResponse> findActiveProducts() {
        log.debug("Fetching all active products");
        return productRepository.findByIsActiveTrue().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get in-stock products
     * @return List of ProductResponse
     */
    public List<ProductResponse> findInStockProducts() {
        log.debug("Fetching in-stock products");
        return productRepository.findInStockProducts().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get out-of-stock products
     * @return List of ProductResponse
     */
    public List<ProductResponse> findOutOfStockProducts() {
        log.debug("Fetching out-of-stock products");
        return productRepository.findOutOfStockProducts().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get products within price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of ProductResponse
     */
    public List<ProductResponse> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.debug("Fetching products within price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Search products by name
     * @param keyword Search keyword
     * @return List of ProductResponse
     */
    public List<ProductResponse> searchByName(String keyword) {
        log.debug("Searching products with keyword: {}", keyword);
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new product
     * @param productRequest Product data
     * @return Created ProductResponse
     * @throws DuplicateResourceException if product name already exists
     */
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        log.debug("Creating new product with name: {}", productRequest.getName());

        // Check if product name already exists
        if (productRepository.existsByName(productRequest.getName())) {
            throw new DuplicateResourceException("Product", "name", productRequest.getName());
        }

        // Check if SKU already exists (if provided)
        if (productRequest.getSku() != null && !productRequest.getSku().isEmpty()
                && productRepository.existsBySku(productRequest.getSku())) {
            throw new DuplicateResourceException("Product", "sku", productRequest.getSku());
        }

        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    /**
     * Update existing product
     * @param id Product ID
     * @param productRequest Updated product data
     * @return Updated ProductResponse
     * @throws ResourceNotFoundException if product not found
     * @throws DuplicateResourceException if product name already exists for another product
     */
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        log.debug("Updating product with id: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        // Check if name is being changed and if it already exists
        if (!existingProduct.getName().equals(productRequest.getName())
                && productRepository.existsByName(productRequest.getName())) {
            throw new DuplicateResourceException("Product", "name", productRequest.getName());
        }

        // Check if SKU is being changed and if it already exists
        if (productRequest.getSku() != null && !productRequest.getSku().isEmpty()
                && !productRequest.getSku().equals(existingProduct.getSku())
                && productRepository.existsBySku(productRequest.getSku())) {
            throw new DuplicateResourceException("Product", "sku", productRequest.getSku());
        }

        productMapper.updateEntityFromRequest(productRequest, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with id: {}", updatedProduct.getId());

        return productMapper.toResponse(updatedProduct);
    }

    /**
     * Delete product by ID
     * @param id Product ID
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional
    public void deleteProduct(Long id) {
        log.debug("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(product);
        log.info("Product deleted successfully with id: {}", id);
    }

    /**
     * Soft delete - deactivate product
     * @param id Product ID
     * @return Deactivated ProductResponse
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional
    public ProductResponse deactivateProduct(Long id) {
        log.debug("Deactivating product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setIsActive(false);
        Product updatedProduct = productRepository.save(product);
        log.info("Product deactivated successfully with id: {}", id);

        return productMapper.toResponse(updatedProduct);
    }

    /**
     * Activate product
     * @param id Product ID
     * @return Activated ProductResponse
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional
    public ProductResponse activateProduct(Long id) {
        log.debug("Activating product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setIsActive(true);
        Product updatedProduct = productRepository.save(product);
        log.info("Product activated successfully with id: {}", id);

        return productMapper.toResponse(updatedProduct);
    }

    /**
     * Update product quantity
     * @param id Product ID
     * @param quantity New quantity
     * @return Updated ProductResponse
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional
    public ProductResponse updateQuantity(Long id, Integer quantity) {
        log.debug("Updating quantity for product id: {} to {}", id, quantity);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setQuantity(quantity);
        Product updatedProduct = productRepository.save(product);
        log.info("Product quantity updated successfully with id: {}", id);

        return productMapper.toResponse(updatedProduct);
    }

    /**
     * Check if product exists by ID
     * @param id Product ID
     * @return true if exists
     */
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    /**
     * Get total product count
     * @return count of products
     */
    public long count() {
        return productRepository.count();
    }

    /**
     * Get active product count
     * @return count of active products
     */
    public long countActive() {
        return productRepository.countByIsActiveTrue();
    }

    /**
     * Get product count by category
     * @param category Product category
     * @return count of products in category
     */
    public long countByCategory(String category) {
        return productRepository.countByCategory(category);
    }
}

