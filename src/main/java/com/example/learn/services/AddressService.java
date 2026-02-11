package com.example.learn.services;

import com.example.learn.dto.AddressRequest;
import com.example.learn.dto.AddressResponse;
import com.example.learn.exceptions.ResourceNotFoundException;
import com.example.learn.mappers.AddressMapper;
import com.example.learn.models.Address;
import com.example.learn.models.User;
import com.example.learn.repositories.AddressRepository;
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
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    /**
     * Get all addresses
     * @return List of AddressResponse
     */
    public List<AddressResponse> findAll() {
        log.debug("Fetching all addresses");
        return addressRepository.findAll().stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get address by ID
     * @param id Address ID
     * @return AddressResponse
     * @throws ResourceNotFoundException if address not found
     */
    public AddressResponse findById(Long id) {
        log.debug("Fetching address with id: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
        return addressMapper.toResponse(address);
    }

    /**
     * Get all addresses for a specific user
     * @param userId User ID
     * @return List of AddressResponse
     */
    public List<AddressResponse> findByUserId(Long userId) {
        log.debug("Fetching addresses for user id: {}", userId);
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return addressRepository.findByUserId(userId).stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get addresses by user ID and address type
     * @param userId User ID
     * @param addressType Address type
     * @return List of AddressResponse
     */
    public List<AddressResponse> findByUserIdAndAddressType(Long userId, String addressType) {
        log.debug("Fetching addresses for user id: {} with type: {}", userId, addressType);
        return addressRepository.findByUserIdAndAddressType(userId, addressType).stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get default address for a user
     * @param userId User ID
     * @return AddressResponse
     * @throws ResourceNotFoundException if default address not found
     */
    public AddressResponse findDefaultAddressByUserId(Long userId) {
        log.debug("Fetching default address for user id: {}", userId);
        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Default Address for User", "userId", userId));
        return addressMapper.toResponse(address);
    }

    /**
     * Get addresses by city
     * @param city City name
     * @return List of AddressResponse
     */
    public List<AddressResponse> findByCity(String city) {
        log.debug("Fetching addresses in city: {}", city);
        return addressRepository.findByCityIgnoreCase(city).stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get addresses by state
     * @param state State name
     * @return List of AddressResponse
     */
    public List<AddressResponse> findByState(String state) {
        log.debug("Fetching addresses in state: {}", state);
        return addressRepository.findByStateIgnoreCase(state).stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get addresses by country
     * @param country Country name
     * @return List of AddressResponse
     */
    public List<AddressResponse> findByCountry(String country) {
        log.debug("Fetching addresses in country: {}", country);
        return addressRepository.findByCountryIgnoreCase(country).stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new address
     * @param addressRequest Address data
     * @return Created AddressResponse
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional
    public AddressResponse createAddress(AddressRequest addressRequest) {
        log.debug("Creating new address for user id: {}", addressRequest.getUserId());

        // Verify user exists
        User user = userRepository.findById(addressRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", addressRequest.getUserId()));

        // If this address is marked as default, unset other default addresses for this user
        if (Boolean.TRUE.equals(addressRequest.getIsDefault())) {
            unsetDefaultAddress(addressRequest.getUserId());
        }

        Address address = addressMapper.toEntity(addressRequest);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        log.info("Address created successfully with id: {}", savedAddress.getId());

        return addressMapper.toResponse(savedAddress);
    }

    /**
     * Update existing address
     * @param id Address ID
     * @param addressRequest Updated address data
     * @return Updated AddressResponse
     * @throws ResourceNotFoundException if address not found
     */
    @Transactional
    public AddressResponse updateAddress(Long id, AddressRequest addressRequest) {
        log.debug("Updating address with id: {}", id);

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

        // If this address is marked as default, unset other default addresses for this user
        if (Boolean.TRUE.equals(addressRequest.getIsDefault()) && !existingAddress.getIsDefault()) {
            unsetDefaultAddress(existingAddress.getUser().getId());
        }

        addressMapper.updateEntityFromRequest(addressRequest, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);
        log.info("Address updated successfully with id: {}", updatedAddress.getId());

        return addressMapper.toResponse(updatedAddress);
    }

    /**
     * Set an address as default for a user
     * @param id Address ID
     * @param userId User ID
     * @return Updated AddressResponse
     * @throws ResourceNotFoundException if address not found
     */
    @Transactional
    public AddressResponse setDefaultAddress(Long id, Long userId) {
        log.debug("Setting address id: {} as default for user id: {}", id, userId);

        Address address = addressRepository.findByUserIdAndAddressId(userId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

        // Unset other default addresses for this user
        unsetDefaultAddress(userId);

        address.setIsDefault(true);
        Address updatedAddress = addressRepository.save(address);
        log.info("Address id: {} set as default for user id: {}", id, userId);

        return addressMapper.toResponse(updatedAddress);
    }

    /**
     * Delete address by ID
     * @param id Address ID
     * @throws ResourceNotFoundException if address not found
     */
    @Transactional
    public void deleteAddress(Long id) {
        log.debug("Deleting address with id: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

        addressRepository.delete(address);
        log.info("Address deleted successfully with id: {}", id);
    }

    /**
     * Delete all addresses for a user
     * @param userId User ID
     */
    @Transactional
    public void deleteAllAddressesByUserId(Long userId) {
        log.debug("Deleting all addresses for user id: {}", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        addressRepository.deleteAll(addresses);
        log.info("All addresses deleted for user id: {}", userId);
    }

    /**
     * Get count of addresses for a user
     * @param userId User ID
     * @return Count of addresses
     */
    public long countAddressesByUserId(Long userId) {
        log.debug("Counting addresses for user id: {}", userId);
        return addressRepository.countByUserId(userId);
    }

    /**
     * Unset default address for a user
     * @param userId User ID
     */
    @Transactional
    public void unsetDefaultAddress(Long userId) {
        addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(address -> {
            address.setIsDefault(false);
            addressRepository.save(address);
            log.debug("Unset default address for user id: {}", userId);
        });
    }
}

