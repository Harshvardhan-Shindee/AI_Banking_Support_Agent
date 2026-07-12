package com.bank.bankingbackend.service.impl;

import com.bank.bankingbackend.dto.AdminStatsResponse;
import com.bank.bankingbackend.dto.AdminUserResponse;
import com.bank.bankingbackend.dto.TransactionResponse;
import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.entity.Transaction;
import com.bank.bankingbackend.exception.ResourceNotFoundException;
import com.bank.bankingbackend.repository.CustomerRepository;
import com.bank.bankingbackend.repository.TransactionRepository;
import com.bank.bankingbackend.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService;


    private AdminUserResponse mapToResponse(Customer user) {

        System.out.println("USER DEBUG: " + user.getCustname());

        String dpUrl = (user.getDp() != null && !user.getDp().isBlank())
                ? "http://localhost:8001/profile/" + user.getDp()
                : null;

        return new AdminUserResponse(
                user.getAccno(),
                user.getCustname(),
                user.getAcctype(),
                user.getEmail(),
                user.getBalance(),
                user.getRole(),
                user.getStatus(),
                dpUrl
        );
    }

    @Override
    public Page<AdminUserResponse> getAllUsers(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("accno").descending());

        Page<Customer> users;

        if (keyword == null || keyword.trim().isEmpty()) {
            users = repository.findByRoleNotAndStatus("ADMIN", "ACTIVE", pageable
            );
        } else {
            users = repository.searchUsers(keyword.trim(), pageable);
        }

        return users.map(this::mapToResponse);
    }


    @Override
    public Map<String, Object> getPendingUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("accno").descending());

        Page<Customer> result = repository.findByStatus("PENDING", pageable);

        List<AdminUserResponse> list = result.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("content", list);
        response.put("currentPage", result.getNumber());
        response.put("totalPages", result.getTotalPages());

        return response;
    }

    @Override
    public Map<String, String> approveUser(Long accno) {
        Customer customer = repository.findById(accno)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        customer.setStatus("ACTIVE");
        repository.save(customer);

        String html = emailService.buildTemplate(
                customer.getCustname(),
                "Your account has been approved successfully.",
                "APPROVED"
        );

        emailService.sendHtmlMail(
                customer.getEmail(),
                "Account Approved",
                html
        );

        return Map.of("message", "User Approved Successfully");
    }

    @Override
    public Map<String, String> rejectUser(Long accno) {
        Customer customer = repository.findById(accno)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        customer.setStatus("REJECTED");
        repository.save(customer);

        String html = emailService.buildTemplate(
                customer.getCustname(),
                "Your account request has been rejected.",
                "REJECTED"
        );

        emailService.sendHtmlMail(
                customer.getEmail(),
                "Account Rejected",
                html
        );

        return Map.of("message", "User Rejected Successfully");
    }

    @Override
    public Map<String, String> blockUser(Long accno) {
        Customer customer = repository.findById(accno)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        customer.setStatus("BLOCKED");
        repository.save(customer);

        String html = emailService.buildTemplate(
                customer.getCustname(),
                "Your account has been temporarily blocked.",
                "BLOCKED"
        );

        emailService.sendHtmlMail(
                customer.getEmail(),
                "Account Blocked",
                html
        );

        return Map.of("message", "User Blocked Successfully");
    }

    @Override
    public Map<String, String> unblockUser(Long accno) {
        Customer customer = repository.findById(accno)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        customer.setStatus("ACTIVE");
        repository.save(customer);

        String html = emailService.buildTemplate(
                customer.getCustname(),
                "Your account is now active again.",
                "UNBLOCKED"
        );

        emailService.sendHtmlMail(
                customer.getEmail(),
                "Account Unblocked",
                html
        );

        return Map.of("message", "User Unblocked Successfully");
    }

    @Override
    public AdminStatsResponse getStats() {

        AdminStatsResponse res = new AdminStatsResponse();

        // 🔥 USER STATS
        res.setTotalUsers(
                repository.countByRoleNot("ADMIN")
        );

        res.setActiveUsers(
                repository.countByStatusAndRoleNot("ACTIVE", "ADMIN")
        );

        res.setBlockedUsers(
                repository.countByStatusAndRoleNot("BLOCKED", "ADMIN")
        );

        res.setPendingUsers(
                repository.countByStatusAndRoleNot("PENDING", "ADMIN")
        );

        res.setRejectedUsers(
                repository.countByStatusAndRoleNot("REJECTED", "ADMIN")
        );

        // 🔥 TRANSACTION STATS
        res.setTotalTransactions(transactionRepository.count());

        // 🔥 TODAY TRANSACTIONS
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        res.setTodayTransactions(
                transactionRepository.countByDateTimeBetween(start, end)
        );

        // 🔥 TOTAL BALANCE
        Double totalBalance = repository.getTotalBalanceWithoutAdmin();
        res.setTotalBalance(totalBalance != null ? totalBalance : 0);

        // 🔥 RECENT TRANSACTIONS (last 5)
        List<Transaction> txns = transactionRepository
                .findAllByOrderByDateTimeDesc(PageRequest.of(0, 5));

        List<TransactionResponse> txnRes = txns.stream()
                .map(t -> new TransactionResponse(
                        t.getTransId(),
                        t.getType(),
                        t.getAmount(),
                        t.getDateTime().toString(),
                        t.getSenderAccno(),
                        t.getReceiverAccno()
                ))
                .toList();

        res.setRecentTransactions(txnRes);

        return res;
    }

    public Page<Customer> getInactiveUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return repository.findInactiveUsers(
                List.of("BLOCKED", "REJECTED"),
                "ADMIN",
                pageable
        );
    }

}