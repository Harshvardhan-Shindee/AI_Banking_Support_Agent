package com.bank.bankingbackend.repository;

import com.bank.bankingbackend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserIdAndStatus(Long userId, String status);
    List<Ticket> findByStatus(String status);
}