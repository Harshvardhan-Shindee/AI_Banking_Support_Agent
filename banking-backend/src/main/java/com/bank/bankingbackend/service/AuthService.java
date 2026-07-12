package com.bank.bankingbackend.service;

import com.bank.bankingbackend.dto.CustomerResponse;
import com.bank.bankingbackend.dto.LoginRequest;
import com.bank.bankingbackend.dto.RegisterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AuthService {
    String login(LoginRequest request);
    CustomerResponse register(RegisterRequest request);
    CustomerResponse getProfile();
    Map<String, String> uploadProfilePicture(MultipartFile file);
}