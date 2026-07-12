package com.bank.bankingbackend.repository;

import com.bank.bankingbackend.entity.SupportAgent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportAgentRepository extends JpaRepository<SupportAgent, Long> {

    List<SupportAgent> findByActiveTrue();

}