package com.bank.bankingbackend.controller;

import com.bank.bankingbackend.entity.Customer;
import com.bank.bankingbackend.entity.SupportAgent;
import com.bank.bankingbackend.entity.Ticket;
import com.bank.bankingbackend.entity.TicketMessage;
import com.bank.bankingbackend.repository.SupportAgentRepository;
import com.bank.bankingbackend.service.CustomerService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.bank.bankingbackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private TicketService tService;

    @Autowired
    private SupportAgentRepository agentRepo;

    @GetMapping("/{accno}")
    public Customer getAccount(@PathVariable Long accno) {
        return service.getAccount(accno);
    }


    @PostMapping("/ticket")
    public Map<String, Object> createTicket(
            @RequestBody Map<String, String> req,
            @RequestHeader("Authorization") String authHeader
    ) {

        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = tService.extractUserIdFromToken(token);

            Ticket ticket = tService.createTicket(userId, req.get("issue"));

            Map<String, Object> response = new HashMap<>();
            SupportAgent agent = agentRepo.findById(ticket.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("Agent not found"));

            response.put("ticketId", ticket.getTicketId());
            response.put("status", ticket.getStatus());
            response.put("assignedTo", agent.getName());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @PostMapping("/ticket/reply/{ticketId}")
    public TicketMessage replyToTicket(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> req,
            @AuthenticationPrincipal Customer user
    ) {
        return tService.replyToTicket(ticketId, req.get("message"), "USER");
    }

    @GetMapping("/ticket/{ticketId}/messages")
    public List<TicketMessage> getMessages(@PathVariable Long ticketId) {
        return tService.getMessages(ticketId);
    }
}