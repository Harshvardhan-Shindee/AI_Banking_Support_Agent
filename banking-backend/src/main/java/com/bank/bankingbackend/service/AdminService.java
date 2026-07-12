package com.bank.bankingbackend.service;

import com.bank.bankingbackend.dto.AdminStatsResponse;
import com.bank.bankingbackend.dto.AdminUserResponse;
import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Page<AdminUserResponse> getAllUsers(String keyword, int page, int size);

    Map<String, Object> getPendingUsers(int page, int size);

    Map<String, String> approveUser(Long accno);

    Map<String, String> rejectUser(Long accno);

    Map<String, String> blockUser(Long accno);

    Map<String, String> unblockUser(Long accno);

    AdminStatsResponse getStats();

    Page<Customer> getInactiveUsers(int page, int size);
}