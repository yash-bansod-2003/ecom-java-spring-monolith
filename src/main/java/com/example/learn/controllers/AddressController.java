package com.example.learn.controllers;

import com.example.learn.dto.AddressRequest;
import com.example.learn.dto.AddressResponse;
import com.example.learn.dto.ApiResponse;
import com.example.learn.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * Get all addresses
     * @return List of all addresses
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAllAddresses() {
        List<AddressResponse> addresses = addressService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("Addresses retrieved successfully", addresses)
        );
    }

    /**
     * Get address by ID
     * @param id Address ID
     * @return Address details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(@PathVariable Long id) {
        AddressResponse address = addressService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Address retrieved successfully", address)
        );
    }

    /**
     * Get all addresses for a specific user
     * @param userId User ID
     * @return List of addresses for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesByUserId(@PathVariable Long userId) {
        List<AddressResponse> addresses = addressService.findByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.success("User addresses retrieved successfully", addresses)
        );
    }

    /**
     * Get addresses by user ID and address type
     * @param userId User ID
     * @param type Address type
     * @return List of addresses matching the criteria
     */
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesByUserIdAndType(
            @PathVariable Long userId,
            @PathVariable String type) {
        List<AddressResponse> addresses = addressService.findByUserIdAndAddressType(userId, type);
        return ResponseEntity.ok(
                ApiResponse.success("Addresses retrieved successfully", addresses)
        );
    }

    /**
     * Get default address for a user
     * @param userId User ID
     * @return Default address for the user
     */
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> getDefaultAddressByUserId(@PathVariable Long userId) {
        AddressResponse address = addressService.findDefaultAddressByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.success("Default address retrieved successfully", address)
        );
    }

    /**
     * Get addresses by city
     * @param city City name
     * @return List of addresses in the city
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesByCity(@PathVariable String city) {
        List<AddressResponse> addresses = addressService.findByCity(city);
        return ResponseEntity.ok(
                ApiResponse.success("Addresses retrieved successfully", addresses)
        );
    }

    /**
     * Get addresses by state
     * @param state State name
     * @return List of addresses in the state
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesByState(@PathVariable String state) {
        List<AddressResponse> addresses = addressService.findByState(state);
        return ResponseEntity.ok(
                ApiResponse.success("Addresses retrieved successfully", addresses)
        );
    }

    /**
     * Get addresses by country
     * @param country Country name
     * @return List of addresses in the country
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesByCountry(@PathVariable String country) {
        List<AddressResponse> addresses = addressService.findByCountry(country);
        return ResponseEntity.ok(
                ApiResponse.success("Addresses retrieved successfully", addresses)
        );
    }

    /**
     * Get count of addresses for a user
     * @param userId User ID
     * @return Count of addresses
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> getAddressCountByUserId(@PathVariable Long userId) {
        long count = addressService.countAddressesByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.success("Address count retrieved successfully", count)
        );
    }

    /**
     * Create new address
     * @param addressRequest Address data
     * @return Created address
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @Valid @RequestBody AddressRequest addressRequest) {
        AddressResponse createdAddress = addressService.createAddress(addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Address created successfully", createdAddress)
        );
    }

    /**
     * Update existing address
     * @param id Address ID
     * @param addressRequest Updated address data
     * @return Updated address
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest addressRequest) {
        AddressResponse updatedAddress = addressService.updateAddress(id, addressRequest);
        return ResponseEntity.ok(
                ApiResponse.success("Address updated successfully", updatedAddress)
        );
    }

    /**
     * Partially update address
     * @param id Address ID
     * @param addressRequest Partial address data
     * @return Updated address
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> partialUpdateAddress(
            @PathVariable Long id,
            @RequestBody AddressRequest addressRequest) {
        AddressResponse updatedAddress = addressService.updateAddress(id, addressRequest);
        return ResponseEntity.ok(
                ApiResponse.success("Address updated successfully", updatedAddress)
        );
    }

    /**
     * Set address as default for a user
     * @param id Address ID
     * @param userId User ID
     * @return Updated address
     */
    @PatchMapping("/{id}/user/{userId}/set-default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @PathVariable Long id,
            @PathVariable Long userId) {
        AddressResponse updatedAddress = addressService.setDefaultAddress(id, userId);
        return ResponseEntity.ok(
                ApiResponse.success("Address set as default successfully", updatedAddress)
        );
    }

    /**
     * Delete address by ID
     * @param id Address ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(
                ApiResponse.success("Address deleted successfully", null)
        );
    }

    /**
     * Delete all addresses for a user
     * @param userId User ID
     * @return Success message
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteAllAddressesByUserId(@PathVariable Long userId) {
        addressService.deleteAllAddressesByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.success("All user addresses deleted successfully", null)
        );
    }
}

