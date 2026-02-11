package com.example.learn.config;

import com.example.learn.models.User;
import com.example.learn.models.UserRole;
import com.example.learn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialize database with sample data
 * This component will run after the application context is loaded
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing database with sample data...");

            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setPhone("1234567890");
            admin.setRole(UserRole.ADMIN);

            User customer1 = new User();
            customer1.setName("John Doe");
            customer1.setEmail("john.doe@example.com");
            customer1.setPhone("9876543210");
            customer1.setRole(UserRole.CUSTOMER);

            User customer2 = new User();
            customer2.setName("Jane Smith");
            customer2.setEmail("jane.smith@example.com");
            customer2.setPhone("5551234567");
            customer2.setRole(UserRole.CUSTOMER);

            userRepository.save(admin);
            userRepository.save(customer1);
            userRepository.save(customer2);

            log.info("Sample data initialized successfully. Created {} users.", userRepository.count());
        } else {
            log.info("Database already contains data. Skipping initialization.");
        }
    }
}

