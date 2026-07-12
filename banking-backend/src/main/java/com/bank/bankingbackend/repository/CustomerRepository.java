package com.bank.bankingbackend.repository;

import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Page<Customer> findByStatus(String status, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.role <> 'ADMIN' AND c.status <> 'PENDING' AND " +
            "(LOWER(c.custname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR STR(c.accno) LIKE CONCAT('%', :keyword, '%'))")
    Page<Customer> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    Page<Customer> findByRoleNotAndStatus(String role, String status, Pageable pageable);

    long countByRoleNot(String role);

    long countByStatusAndRoleNot(String status, String role);

    @Query("SELECT SUM(c.balance) FROM Customer c WHERE c.role <> 'ADMIN'")
    Double getTotalBalanceWithoutAdmin();

    @Query("SELECT c FROM Customer c WHERE c.status IN :statuses AND c.role <> :role")
    Page<Customer> findInactiveUsers(
            @Param("statuses") List<String> statuses,
            @Param("role") String role,
            Pageable pageable
    );
}