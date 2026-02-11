package com.example.learn.mappers;

import com.example.learn.dto.ProductRequest;
import com.example.learn.dto.ProductResponse;
import com.example.learn.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    /**
     * Convert ProductRequest to Product entity
     */
    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setSku(request.getSku());
        product.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        return product;
    }

    /**
     * Convert Product entity to ProductResponse
     */
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setCategory(product.getCategory());
        response.setSku(product.getSku());
        response.setIsActive(product.getIsActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }

    /**
     * Update existing Product entity from ProductRequest
     */
    public void updateEntityFromRequest(ProductRequest request, Product product) {
        if (request == null || product == null) {
            return;
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setSku(request.getSku());
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }
    }
}

