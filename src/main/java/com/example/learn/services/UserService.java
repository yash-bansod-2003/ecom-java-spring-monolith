package com.example.learn.services;

import com.example.learn.dto.UserRequest;
import com.example.learn.dto.UserResponse;
import com.example.learn.exceptions.DuplicateResourceException;
import com.example.learn.exceptions.ResourceNotFoundException;
import com.example.learn.mappers.UserMapper;
import com.example.learn.models.User;
import com.example.learn.models.UserRole;
import com.example.learn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Get all users
     * @return List of UserResponse
     */
    public List<UserResponse> findAll() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get user by ID
     * @param id User ID
     * @return UserResponse
     * @throws ResourceNotFoundException if user not found
     */
    public UserResponse findById(Long id) {
        log.debug("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toResponse(user);
    }

    /**
     * Get user by email
     * @param email User email
     * @return UserResponse
     * @throws ResourceNotFoundException if user not found
     */
    public UserResponse findByEmail(String email) {
        log.debug("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userMapper.toResponse(user);
    }

    /**
     * Get users by role
     * @param role UserRole
     * @return List of UserResponse
     */
    public List<UserResponse> findByRole(UserRole role) {
        log.debug("Fetching users with role: {}", role);
        return userRepository.findByRole(role).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Search users by name
     * @param name Search term
     * @return List of UserResponse
     */
    public List<UserResponse> searchByName(String name) {
        log.debug("Searching users with name containing: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new user
     * @param userRequest User data
     * @return Created UserResponse
     * @throws DuplicateResourceException if email already exists
     */
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        log.debug("Creating new user with email: {}", userRequest.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("User", "email", userRequest.getEmail());
        }

        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    /**
     * Update existing user
     * @param id User ID
     * @param userRequest Updated user data
     * @return Updated UserResponse
     * @throws ResourceNotFoundException if user not found
     * @throws DuplicateResourceException if email already exists for another user
     */
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        log.debug("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(userRequest.getEmail())
                && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("User", "email", userRequest.getEmail());
        }

        userMapper.updateEntityFromRequest(userRequest, existingUser);
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with id: {}", updatedUser.getId());

        return userMapper.toResponse(updatedUser);
    }

    /**
     * Delete user by ID
     * @param id User ID
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional
    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);
        log.info("User deleted successfully with id: {}", id);
    }

    /**
     * Check if user exists by ID
     * @param id User ID
     * @return true if exists
     */
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * Get total user count
     * @return count of users
     */
    public long count() {
        return userRepository.count();
    }
}
