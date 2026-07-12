package com.bank.bankingbackend.controller;

import com.bank.bankingbackend.dto.AdminStatsResponse;
import com.bank.bankingbackend.dto.AdminUserResponse;
import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.entity.Ticket;
import com.bank.bankingbackend.entity.TicketMessage;
import com.bank.bankingbackend.entity.Transaction;
import com.bank.bankingbackend.service.AdminService;

import com.bank.bankingbackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @Autowired
    private TicketService tService;

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AdminUserResponse> users = service.getAllUsers(keyword, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", users.getContent());
        response.put("totalPages", users.getTotalPages());
        response.put("totalElements", users.getTotalElements());
        response.put("currentPage", users.getNumber());

        return response;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public Map<String, Object> getPendingUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.getPendingUsers(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{accno}")
    public Map<String, String> approveUser(@PathVariable Long accno) {
        return service.approveUser(accno);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{accno}")
    public Map<String, String> rejectUser(@PathVariable Long accno) {
        return service.rejectUser(accno);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/block/{accno}")
    public Map<String, String> blockUser(@PathVariable Long accno) {
        return service.blockUser(accno);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/unblock/{accno}")
    public Map<String, String> unblockUser(@PathVariable Long accno) {
        return service.unblockUser(accno);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public AdminStatsResponse getStats() {
        return service.getStats();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/inactive-users")
    public Page<Customer> getInactiveUsers(@RequestParam int page, @RequestParam int size
    ) {
        try {
            return service.getInactiveUsers(page, size);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ================= TICKETS =================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/tickets")
    public List<Ticket> getAllTickets() {
        tService.autoEscalateTickets();
        return tService.getAllTickets();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tickets/resolve/{id}")
    public Map<String, String> resolveTicket(@PathVariable Long id) {
        return tService.resolveTicket(id);
    }

    @GetMapping("/tickets/filter")
    public List<Ticket> filterTickets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority
    ) {
        return tService.filterTickets(status, priority);
    }

    @PutMapping("/tickets/reply/{ticketId}")
    public TicketMessage replyToTicket(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> req
    ) {
        return tService.replyToTicket(ticketId, req.get("message"), "AGENT");
    }
}