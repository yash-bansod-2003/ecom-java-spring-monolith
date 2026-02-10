package com.example.learn.services;

import com.example.learn.models.User;
import com.example.learn.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return  this.userRepository.findAll();
    }

    public void addUser(User user) {
        this.userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
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
}
