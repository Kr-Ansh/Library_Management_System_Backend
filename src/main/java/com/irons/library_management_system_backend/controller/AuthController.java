package com.irons.library_management_system_backend.controller;

import com.irons.library_management_system_backend.dto.AuthRequestDTO;
import com.irons.library_management_system_backend.dto.JwtResponseDTO;
import com.irons.library_management_system_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AuthRequestDTO request) {

        authService.registerUser(request);
        return new ResponseEntity<>("User registered successfully. You can now log in.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> loginUser(@Valid @RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.authenticateUser(request));
    }
}
