package com.example.chess.chess_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chess.chess_backend.dto.JwtAuthResponse;
import com.example.chess.chess_backend.dto.LoginRequest;
import com.example.chess.chess_backend.entity.AppUser;
import com.example.chess.chess_backend.security.JwtTokenProvider; // Changed from JwtUtil
import com.example.chess.chess_backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Changed from JwtUtil

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody AppUser user) {
    AppUser newUser = userService.register(user);
    Map<String, String> response = new HashMap<>();
    response.put("message", "User registered successfully!");
    return ResponseEntity.ok(response);
}


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AppUser user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.checkPassword(user, request.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername()); // Changed from jwtUtil
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
}