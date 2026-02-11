package com.example.learn.controllers;

import com.example.learn.dto.ApiResponse;
import com.example.learn.dto.UserRequest;
import com.example.learn.dto.UserResponse;
import com.example.learn.models.UserRole;
import com.example.learn.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get all users
     * @return List of all users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(
            ApiResponse.success("Users retrieved successfully", users)
        );
    }

    /**
     * Get user by ID
     * @param id User ID
     * @return User details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(
            ApiResponse.success("User retrieved successfully", user)
        );
    }

    /**
     * Get user by email
     * @param email User email
     * @return User details
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        UserResponse user = userService.findByEmail(email);
        return ResponseEntity.ok(
            ApiResponse.success("User retrieved successfully", user)
        );
    }

    /**
     * Get users by role
     * @param role User role
     * @return List of users with specified role
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable UserRole role) {
        List<UserResponse> users = userService.findByRole(role);
        return ResponseEntity.ok(
            ApiResponse.success("Users retrieved successfully", users)
        );
    }

    /**
     * Search users by name
     * @param name Search term
     * @return List of matching users
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsersByName(
            @RequestParam(name = "name") String name) {
        List<UserResponse> users = userService.searchByName(name);
        return ResponseEntity.ok(
            ApiResponse.success("Search completed successfully", users)
        );
    }

    /**
     * Create new user
     * @param userRequest User data
     * @return Created user
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("User created successfully", createdUser)
        );
    }

    /**
     * Update existing user
     * @param id User ID
     * @param userRequest Updated user data
     * @return Updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(
            ApiResponse.success("User updated successfully", updatedUser)
        );
    }

    /**
     * Delete user
     * @param id User ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
            ApiResponse.success("User deleted successfully", null)
        );
    }

    /**
     * Get total user count
     * @return Count of users
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getUserCount() {
        long count = userService.count();
        return ResponseEntity.ok(
            ApiResponse.success("User count retrieved successfully", count)
        );
    }
}
