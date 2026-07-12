package com.bank.bankingbackend.service;

import com.bank.bankingbackend.entity.Transaction;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    Map<String, String> deposit(Long accno, double amount);

    Map<String, String> withdraw(Long accno, double amount);

    Map<String, String> transfer(Long senderAccno, Long receiverAccno, double amount);

    List<Transaction> getAllTransactions(Long accno);

    List<Transaction> getMiniStatement(Long accno);

    Map<String, Object> getUserTransactions(Long accno, int page, int size);
}