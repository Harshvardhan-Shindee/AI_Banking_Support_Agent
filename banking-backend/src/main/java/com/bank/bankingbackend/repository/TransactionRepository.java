package com.bank.bankingbackend.repository;

import com.bank.bankingbackend.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccnoOrReceiverAccnoOrderByDateTimeDesc(
            Long sender, Long receiver);

    Page<Transaction> findBySenderAccnoOrReceiverAccnoOrderByDateTimeDesc(
            Long sender, Long receiver, Pageable pageable
    );

    long countByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Transaction> findAllByOrderByDateTimeDesc(Pageable pageable);
}