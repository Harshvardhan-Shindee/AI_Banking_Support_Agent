package com.bank.bankingbackend.controller;

import com.bank.bankingbackend.dto.CustomerResponse;
import com.bank.bankingbackend.dto.LoginRequest;
import com.bank.bankingbackend.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bank.bankingbackend.service.AuthService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/register")
    public CustomerResponse register(@Valid @RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @GetMapping("/profile")
    public CustomerResponse getProfile() {
        return service.getProfile();
    }

    @PostMapping("/upload-dp")
    public Map<String, String> uploadDp(@RequestParam("file") MultipartFile file) {
        return service.uploadProfilePicture(file);
    }
}