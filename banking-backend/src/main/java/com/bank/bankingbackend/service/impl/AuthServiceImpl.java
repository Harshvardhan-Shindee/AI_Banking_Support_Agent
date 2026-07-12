package com.bank.bankingbackend.service.impl;

import com.bank.bankingbackend.dto.CustomerResponse;
import com.bank.bankingbackend.dto.LoginRequest;
import com.bank.bankingbackend.dto.RegisterRequest;
import com.bank.bankingbackend.exception.BadRequestException;
import com.bank.bankingbackend.exception.InternalServerException;
import com.bank.bankingbackend.exception.ResourceNotFoundException;
import com.bank.bankingbackend.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.repository.CustomerRepository;
import com.bank.bankingbackend.service.AuthService;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Override
    public String login(LoginRequest request) {

        Customer customer = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String status = customer.getStatus();

        if (status.equals("PENDING")) {
            throw new BadRequestException("Your account is under admin review.");
        }

        if (status.equals("REJECTED")) {
            throw new BadRequestException("Your account is rejected.");
        }

        if (status.equals("BLOCKED")) {
            throw new BadRequestException("Your account is blocked.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return jwtUtil.generateToken(customer.getEmail(), customer.getRole(), customer.getAccno());
    }

    @Override
    public CustomerResponse register(RegisterRequest request) {

        // 🔥 Prevent duplicate email
        if (repository.findByEmail(request.getEmail()).isPresent())
            throw new BadRequestException("Email already registered");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        Customer customer = new Customer();

        customer.setCustname(request.getCustname());
        customer.setAcctype(request.getAcctype());
        customer.setEmail(request.getEmail().toLowerCase());
        customer.setPassword(encoder.encode(request.getPassword()));
        customer.setRole("CUSTOMER");
        customer.setStatus("PENDING");
        customer.setBalance(0);
        customer.setDp(null);   // 🔥 No DP initially

        Customer saved = repository.save(customer);

        String html = emailService.buildTemplate(
                saved.getCustname(),
                "Your account has been created successfully and is under admin review.",
                "PENDING"
        );

        emailService.sendHtmlMail(
                saved.getEmail(),
                "Account Created",
                html
        );

        return new CustomerResponse(
                saved.getAccno(),
                saved.getCustname(),
                saved.getAcctype(),
                saved.getEmail(),
                saved.getBalance(),
                saved.getRole(),
                saved.getStatus(),
                null   // 🔥 DP URL null at registration
        );
    }

    @Override
    public CustomerResponse getProfile() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Customer customer = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String dpUrl = customer.getDp() != null ?
                "http://localhost:8001/profile/" + customer.getDp()
                : null;

        return new CustomerResponse(
                customer.getAccno(),
                customer.getCustname(),
                customer.getAcctype(),
                customer.getEmail(),
                customer.getBalance(),
                customer.getRole(),
                customer.getStatus(),
                dpUrl
        );
    }

    @Override
    public Map<String, String> uploadProfilePicture(MultipartFile file) {

        if (file.isEmpty())
            throw new BadRequestException("File is empty");

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png")))
            throw new RuntimeException("Only JPG and PNG allowed");

        if (file.getSize() > 2 * 1024 * 1024)
            throw new RuntimeException("File size must be less than 2MB");

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Customer customer = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        try {

            String uploadDir = "uploads/profile/";
            File directory = new File(uploadDir);
            if (!directory.exists())
                directory.mkdirs();

            // 🔥 DELETE OLD FILE IF EXISTS
            if (customer.getDp() != null) {
                File oldFile = new File(uploadDir + customer.getDp());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // 🔥 CREATE UNIQUE FILE NAME
            String original = file.getOriginalFilename();

            if (original == null || !original.contains("."))
                throw new BadRequestException("Invalid file name");

            String extension = original.substring(original.lastIndexOf("."));

            String fileName = customer.getAccno() + "_dp" + extension;

            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());

            customer.setDp(fileName);
            repository.save(customer);

            return Map.of("message", "Profile picture uploaded successfully");

        } catch (IOException e) {
            throw new InternalServerException("Failed to upload file");
        }
    }
}