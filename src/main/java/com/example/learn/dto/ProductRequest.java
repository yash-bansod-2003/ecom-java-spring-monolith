package com.example.learn.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have maximum 8 integer digits and 2 fractional digits")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 999999, message = "Quantity cannot exceed 999999")
    private Integer quantity;

    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @Pattern(regexp = "^[A-Z0-9-]*$", message = "SKU must contain only uppercase letters, numbers, and hyphens")
    @Size(max = 20, message = "SKU must not exceed 20 characters")
    private String sku;

    private Boolean isActive = true;
}

