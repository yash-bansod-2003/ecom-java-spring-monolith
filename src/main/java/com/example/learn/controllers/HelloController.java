package com.example.learn.controllers;

import com.example.learn.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * Health check endpoint
     * @return Welcome message
     */
    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<Map<String, String>>> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello World");
        response.put("status", "Application is running");
        return ResponseEntity.ok(
            ApiResponse.success("Welcome to User Management API", response)
        );
    }

    /**
     * Personalized greeting endpoint
     * @param name User's name
     * @return Greeting message
     */
    @PostMapping("/greet")
    public ResponseEntity<ApiResponse<Map<String, String>>> greeting(@RequestParam String name) {
        Map<String, String> response = new HashMap<>();
        response.put("greeting", "Hello, " + name + "!");
        return ResponseEntity.ok(
            ApiResponse.success("Greeting generated successfully", response)
        );
    }
}
