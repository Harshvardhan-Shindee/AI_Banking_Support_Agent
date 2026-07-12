package com.bank.bankingbackend.controller;

import com.bank.bankingbackend.dto.DepositRequest;
import com.bank.bankingbackend.dto.TransferRequest;
import com.bank.bankingbackend.dto.WithdrawRequest;
import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.entity.Transaction;
import com.bank.bankingbackend.repository.CustomerRepository;
import com.bank.bankingbackend.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private CustomerRepository cRepository;
    @PostMapping("/deposit")
    public Map<String, String> deposit(@RequestBody DepositRequest request) {
        return service.deposit(request.getAccno(), request.getAmount());
    }

    @PostMapping("/transfer")
    public Map<String, String> transfer(
            @RequestBody TransferRequest request
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Customer sender = cRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.transfer(
                sender.getAccno(),   // ✅ FIXED
                request.getReceiverAccno(),
                request.getAmount()
        );
    }

    @PostMapping("/withdraw")
    public Map<String, String> withdraw(@RequestBody WithdrawRequest request) {
        return service.withdraw(request.getAccno(), request.getAmount());
    }

    @GetMapping("/{accno}")
    public List<Transaction> getAll(@PathVariable Long accno) {
        return service.getAllTransactions(accno);
    }

    @GetMapping("/mini/{accno}")
    public List<Transaction> getMini(@PathVariable Long accno) {
        return service.getMiniStatement(accno);
    }

    @GetMapping("/user/{accno}")
    public Map<String, Object> getUserTransactions(
            @PathVariable Long accno,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.getUserTransactions(accno, page, size);
    }
}