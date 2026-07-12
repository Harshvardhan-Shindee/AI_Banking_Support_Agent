package com.bank.bankingbackend.service.impl;

import com.bank.bankingbackend.dto.TransactionResponse;
import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.entity.Transaction;
import com.bank.bankingbackend.exception.BadRequestException;
import com.bank.bankingbackend.exception.ResourceNotFoundException;
import com.bank.bankingbackend.exception.UnauthorizedException;
import com.bank.bankingbackend.repository.CustomerRepository;
import com.bank.bankingbackend.repository.TransactionRepository;
import com.bank.bankingbackend.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private CustomerRepository cRepository;

    @Autowired
    private TransactionRepository tRepository;

    private String getLoggedInEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void validateAccess(Long accno) {

        String email = getLoggedInEmail();

        Customer loggedInUser = cRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!isAdmin() && !loggedInUser.getAccno().equals(accno)) {
            throw new UnauthorizedException("Access denied");
        }
    }

    @Override
    @Transactional
    public Map<String, String> deposit(Long accno, double amount) {

        if (amount <= 0)
            throw new BadRequestException("Invalid deposit amount");

        validateAccess(accno);

        Customer customer = cRepository.findById(accno)
                .orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));

        if (!customer.getStatus().equals("ACTIVE"))
            throw new BadRequestException("Account not active");

        customer.setBalance(customer.getBalance() + amount);
        cRepository.save(customer);

        Transaction t = new Transaction();
        t.setSenderAccno(accno);
        t.setReceiverAccno(accno);
        t.setAmount(amount);
        t.setType("DEPOSIT");
        t.setDateTime(LocalDateTime.now());

        tRepository.save(t);

        return Map.of("message", "Deposit Successful");
    }

    @Override
    @Transactional
    public Map<String, String> withdraw(Long accno, double amount) {

        if (amount <= 0)
            throw new BadRequestException("Invalid withdrawal amount");

        validateAccess(accno);

        Customer customer = cRepository.findById(accno)
                .orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));

        if (!customer.getStatus().equals("ACTIVE"))
            throw new BadRequestException("Account not active");

        if (customer.getBalance() < amount)
            throw new BadRequestException("Insufficient Balance");

        customer.setBalance(customer.getBalance() - amount);
        cRepository.save(customer);

        Transaction t = new Transaction();
        t.setSenderAccno(accno);
        t.setReceiverAccno(accno);
        t.setAmount(amount);
        t.setType("WITHDRAW");
        t.setDateTime(LocalDateTime.now());

        tRepository.save(t);

        return Map.of("message", "Withdrawal Successful");
    }

    @Override
    @Transactional
    public Map<String, String> transfer(Long senderAccno, Long receiverAccno, double amount) {

        if (amount <= 0)
            throw new BadRequestException("Invalid transfer amount");

        if (senderAccno.equals(receiverAccno))
            throw new BadRequestException("Cannot transfer to same account");

        validateAccess(senderAccno);

        Customer sender = cRepository.findById(senderAccno)
                .orElseThrow(() -> new ResourceNotFoundException("Sender Not Found"));

        Customer receiver = cRepository.findById(receiverAccno)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver Not Found"));

        if (!sender.getStatus().equals("ACTIVE") ||
                !receiver.getStatus().equals("ACTIVE"))
            throw new BadRequestException("Account not active");

        if (sender.getBalance() < amount)
            throw new BadRequestException("Insufficient Balance");

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        cRepository.save(sender);
        cRepository.save(receiver);

        Transaction t = new Transaction();
        t.setSenderAccno(senderAccno);
        t.setReceiverAccno(receiverAccno);
        t.setAmount(amount);
        t.setType("TRANSFER");
        t.setDateTime(LocalDateTime.now());

        tRepository.save(t);

        return Map.of("message", "Transfer Successful");
    }

    @Override
    public List<Transaction> getAllTransactions(Long accno) {

        validateAccess(accno);

        return tRepository
                .findBySenderAccnoOrReceiverAccnoOrderByDateTimeDesc(accno, accno);
    }

    @Override
    public List<Transaction> getMiniStatement(Long accno) {
        List<Transaction> list = getAllTransactions(accno);
        return list.size() > 5 ? list.subList(0, 5) : list;
    }

    @Override
    public Map<String, Object> getUserTransactions(Long accno, int page, int size) {

        Page<Transaction> result = tRepository
                .findBySenderAccnoOrReceiverAccnoOrderByDateTimeDesc(
                        accno, accno, PageRequest.of(page, size));

        List<TransactionResponse> list = result.getContent().stream()
                .map(t -> new TransactionResponse(
                        t.getTransId(),
                        t.getType(),
                        t.getAmount(),
                        t.getDateTime().toString(),
                        t.getSenderAccno(),
                        t.getReceiverAccno()
                ))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("content", list);
        response.put("currentPage", result.getNumber());
        response.put("totalPages", result.getTotalPages());

        return response;
    }
}