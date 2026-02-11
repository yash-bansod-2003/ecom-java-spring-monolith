package com.example.learn.repositories;

import com.example.learn.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    List<Address> findByUserIdAndAddressType(Long userId, String addressType);

    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.id = :addressId")
    Optional<Address> findByUserIdAndAddressId(Long userId, Long addressId);

    List<Address> findByCityIgnoreCase(String city);

    List<Address> findByStateIgnoreCase(String state);

    List<Address> findByCountryIgnoreCase(String country);

    boolean existsByUserIdAndId(Long userId, Long addressId);

    long countByUserId(Long userId);
}

