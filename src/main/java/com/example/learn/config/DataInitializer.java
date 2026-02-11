package com.example.learn.config;

import com.example.learn.models.Address;
import com.example.learn.models.User;
import com.example.learn.models.UserRole;
import com.example.learn.repositories.AddressRepository;
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
    private final AddressRepository addressRepository;

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

            // Create sample addresses
            Address adminAddress = new Address();
            adminAddress.setStreet("100 Admin Plaza");
            adminAddress.setCity("New York");
            adminAddress.setState("NY");
            adminAddress.setZipCode("10001");
            adminAddress.setCountry("USA");
            adminAddress.setAddressType("WORK");
            adminAddress.setIsDefault(true);
            adminAddress.setUser(admin);

            Address customer1HomeAddress = new Address();
            customer1HomeAddress.setStreet("123 Main Street");
            customer1HomeAddress.setCity("Los Angeles");
            customer1HomeAddress.setState("CA");
            customer1HomeAddress.setZipCode("90001");
            customer1HomeAddress.setCountry("USA");
            customer1HomeAddress.setAddressType("HOME");
            customer1HomeAddress.setIsDefault(true);
            customer1HomeAddress.setUser(customer1);

            Address customer1WorkAddress = new Address();
            customer1WorkAddress.setStreet("456 Business Blvd");
            customer1WorkAddress.setCity("Los Angeles");
            customer1WorkAddress.setState("CA");
            customer1WorkAddress.setZipCode("90002");
            customer1WorkAddress.setCountry("USA");
            customer1WorkAddress.setAddressType("WORK");
            customer1WorkAddress.setIsDefault(false);
            customer1WorkAddress.setUser(customer1);

            Address customer2Address = new Address();
            customer2Address.setStreet("789 Oak Avenue");
            customer2Address.setCity("Chicago");
            customer2Address.setState("IL");
            customer2Address.setZipCode("60601");
            customer2Address.setCountry("USA");
            customer2Address.setAddressType("HOME");
            customer2Address.setIsDefault(true);
            customer2Address.setUser(customer2);

            addressRepository.save(adminAddress);
            addressRepository.save(customer1HomeAddress);
            addressRepository.save(customer1WorkAddress);
            addressRepository.save(customer2Address);

            log.info("Sample addresses initialized successfully. Created {} addresses.", addressRepository.count());
        } else {
            log.info("Database already contains data. Skipping initialization.");
        }
    }
}

