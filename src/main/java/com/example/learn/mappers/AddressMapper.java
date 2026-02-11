package com.example.learn.mappers;

import com.example.learn.dto.AddressRequest;
import com.example.learn.dto.AddressResponse;
import com.example.learn.models.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse toResponse(Address address) {
        if (address == null) {
            return null;
        }

        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setUserId(address.getUser().getId());
        response.setUserName(address.getUser().getName());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setZipCode(address.getZipCode());
        response.setCountry(address.getCountry());
        response.setAddressType(address.getAddressType());
        response.setIsDefault(address.getIsDefault());
        response.setCreatedAt(address.getCreatedAt());
        response.setUpdatedAt(address.getUpdatedAt());
        return response;
    }

    public Address toEntity(AddressRequest request) {
        if (request == null) {
            return null;
        }

        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setCountry(request.getCountry());
        address.setAddressType(request.getAddressType());
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        return address;
    }

    public void updateEntityFromRequest(AddressRequest request, Address address) {
        if (request == null || address == null) {
            return;
        }

        if (request.getStreet() != null) {
            address.setStreet(request.getStreet());
        }
        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }
        if (request.getState() != null) {
            address.setState(request.getState());
        }
        if (request.getZipCode() != null) {
            address.setZipCode(request.getZipCode());
        }
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }
        if (request.getAddressType() != null) {
            address.setAddressType(request.getAddressType());
        }
        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }
    }
}

