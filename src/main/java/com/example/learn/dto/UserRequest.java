package com.example.learn.dto;

import com.example.learn.models.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String email;
    private String phone;
}
