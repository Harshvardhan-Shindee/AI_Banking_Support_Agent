package com.bank.bankingbackend.service.impl;

import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.exception.BadRequestException;
import com.bank.bankingbackend.exception.ResourceNotFoundException;
import com.bank.bankingbackend.exception.UnauthorizedException;
import com.bank.bankingbackend.repository.CustomerRepository;
import com.bank.bankingbackend.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer getAccount(Long accno) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = auth.getName();

        Customer requested = repository.findById(accno)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // ADMIN can access anyone
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return requested;
        }

        // USER can access only own account
        if (!requested.getEmail().equals(loggedInEmail)) {
            throw new UnauthorizedException("Access denied");
        }

        return requested;
    }



}