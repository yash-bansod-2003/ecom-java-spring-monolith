package com.example.learn.controllers;

import com.example.learn.dto.AddressRequest;
import com.example.learn.dto.AddressResponse;
import com.example.learn.services.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private AddressResponse addressResponse;
    private List<AddressResponse> addressResponseList;

    @BeforeEach
    void setUp() {
        addressResponse = new AddressResponse();
        addressResponse.setId(1L);
        addressResponse.setUserId(1L);
        addressResponse.setUserName("John Doe");
        addressResponse.setStreet("123 Main St");
        addressResponse.setCity("New York");
        addressResponse.setState("NY");
        addressResponse.setZipCode("10001");
        addressResponse.setCountry("USA");
        addressResponse.setAddressType("HOME");
        addressResponse.setIsDefault(true);
        addressResponse.setCreatedAt(LocalDateTime.now());
        addressResponse.setUpdatedAt(LocalDateTime.now());

        addressResponseList = Arrays.asList(addressResponse);
    }

    @Test
    void getAllAddresses_ShouldReturnAddressList() throws Exception {
        when(addressService.findAll()).thenReturn(addressResponseList);

        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Addresses retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getAddressById_ShouldReturnAddress() throws Exception {
        when(addressService.findById(1L)).thenReturn(addressResponse);

        mockMvc.perform(get("/api/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.street").value("123 Main St"));
    }

    @Test
    void getAddressesByUserId_ShouldReturnUserAddresses() throws Exception {
        when(addressService.findByUserId(1L)).thenReturn(addressResponseList);

        mockMvc.perform(get("/api/addresses/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void createAddress_ShouldReturnCreatedAddress() throws Exception {
        String addressJson = """
                {
                    "userId": 1,
                    "street": "123 Main St",
                    "city": "New York",
                    "state": "NY",
                    "zipCode": "10001",
                    "country": "USA",
                    "addressType": "HOME",
                    "isDefault": true
                }
                """;

        when(addressService.createAddress(any(AddressRequest.class))).thenReturn(addressResponse);

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Address created successfully"));
    }

    @Test
    void updateAddress_ShouldReturnUpdatedAddress() throws Exception {
        String addressJson = """
                {
                    "userId": 1,
                    "street": "456 Updated St",
                    "city": "New York",
                    "state": "NY",
                    "zipCode": "10001",
                    "country": "USA"
                }
                """;

        when(addressService.updateAddress(eq(1L), any(AddressRequest.class))).thenReturn(addressResponse);

        mockMvc.perform(put("/api/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Address updated successfully"));
    }

    @Test
    void deleteAddress_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/api/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Address deleted successfully"));
    }
}

