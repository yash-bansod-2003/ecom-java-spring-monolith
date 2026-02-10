package com.example.learn.services;

import com.example.learn.dto.UserResponse;
import com.example.learn.models.User;
import com.example.learn.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> findAll() {
        // using method reference instead of the lambada
        return this.userRepository.findAll().stream()
                .map(this::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(User user) {
        this.userRepository.save(user);
    }

    public Optional<UserResponse> findById(Long id) {
        return this.userRepository.findById(id).stream()
                .map(this::mapUserToUserResponse)
                .findFirst();
    }

    public boolean updateUser(Long id, User user) {
        return this.userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    this.userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        return userResponse;
    }
}
