package com.bank.bankingbackend.service.impl;

import com.bank.bankingbackend.entity.*;
import com.bank.bankingbackend.repository.*;
import com.bank.bankingbackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository repo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private SupportAgentRepository agentRepo;

    @Autowired
    private TicketEmailService ticketEmailService;

    @Autowired
    private TicketMessageRepository messageRepo;

    // ================= CREATE TICKET =================
    @Override
    public Ticket createTicket(Long userId, String issue) {

        List<Ticket> existing = repo.findByUserIdAndStatus(userId, "OPEN");

//        if (!existing.isEmpty()) {
//            return existing.get(0); // better UX
//        }

        Ticket t = new Ticket();
        t.setUserId(userId);
        t.setIssue(issue);
        t.setStatus("OPEN");

        // PRIORITY
        if (issue.toLowerCase().contains("fraud") ||
                issue.toLowerCase().contains("money deducted")) {
            t.setPriority("HIGH");
        } else {
            t.setPriority("NORMAL");
        }

        // ASSIGN AGENT
        Long agentId = getNextAvailableAgent();
        t.setAssignedTo(agentId);

        Ticket saved = repo.save(t);

        // INITIAL MESSAGE
        TicketMessage msg = new TicketMessage();
        msg.setTicketId(saved.getId());
        msg.setMessage(issue);
        msg.setSenderType("USER");
        msg.setCreatedAt(LocalDateTime.now());

        messageRepo.save(msg);

        // SEND EMAIL
        try {
            SupportAgent agent = agentRepo.findById(agentId)
                    .orElseThrow(() -> new RuntimeException("Agent not found"));

            sendTicketEmail(saved, agent);

        } catch (Exception e) {
            System.out.println("EMAIL ERROR: " + e.getMessage());
        }

        return saved;
    }

    // ================= GET ALL =================
    @Override
    public List<Ticket> getAllTickets() {
        return repo.findAll();
    }

    // ================= RESOLVE =================
    @Override
    public Map<String, String> resolveTicket(Long id) {

        Ticket t = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        t.setStatus("RESOLVED");
        repo.save(t);

        return Map.of("message", "Ticket resolved");
    }

    // ================= AUTO ESCALATION =================
    @Override
    public void autoEscalateTickets() {

        List<Ticket> tickets = repo.findByStatus("OPEN");

        for (Ticket t : tickets) {

            long hours = java.time.Duration.between(
                    t.getCreatedAt(),
                    LocalDateTime.now()
            ).toHours();

            if (hours >= 24) {
                t.setPriority("HIGH");
                t.setAssignedTo(getSeniorAgentId());
                repo.save(t);
            }
        }
    }

    // ================= FILTER =================
    @Override
    public List<Ticket> filterTickets(String status, String priority) {

        List<Ticket> tickets = repo.findAll();

        if (status != null) {
            tickets = tickets.stream()
                    .filter(t -> status.equalsIgnoreCase(t.getStatus()))
                    .toList();
        }

        if (priority != null) {
            tickets = tickets.stream()
                    .filter(t -> priority.equalsIgnoreCase(t.getPriority()))
                    .toList();
        }

        return tickets;
    }

    // ================= AGENT ASSIGN =================
    @Override
    public Long getNextAvailableAgent() {

        List<SupportAgent> agents = agentRepo.findByActiveTrue();

        if (agents.isEmpty()) {
            throw new RuntimeException("No support agents available");
        }

        return agents.get(new java.util.Random().nextInt(agents.size())).getId();
    }

    // ================= SENIOR AGENT =================
    public Long getSeniorAgentId() {

        List<SupportAgent> agents = agentRepo.findByActiveTrue();

        if (agents.isEmpty()) {
            throw new RuntimeException("No agents available");
        }

        return agents.get(0).getId(); // later filter role=SENIOR
    }

    // ================= EMAIL =================
    @Override
    public void sendTicketEmail(Ticket ticket, SupportAgent agent) {

        Customer customer = customerRepo.findById(ticket.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ticketEmailService.sendSupportEmail(
                customer.getEmail(),
                agent.getName(),
                ticket.getIssue(),
                ticket.getId()
        );
    }

    // ================= REPLY =================
    @Override
    public TicketMessage replyToTicket(Long ticketId, String message, String sender) {

        Ticket ticket = repo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if ("RESOLVED".equals(ticket.getStatus())) {
            throw new RuntimeException("Ticket already resolved");
        }

        TicketMessage msg = new TicketMessage();
        msg.setTicketId(ticketId);
        msg.setMessage(message);
        msg.setSenderType(sender);
        msg.setCreatedAt(LocalDateTime.now());

        TicketMessage saved = messageRepo.save(msg);

        // EMAIL ONLY WHEN AGENT REPLIES
        if ("AGENT".equals(sender)) {

            Customer customer = customerRepo.findById(ticket.getUserId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            SupportAgent agent = agentRepo.findById(ticket.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("Agent not found"));

            try {
                ticketEmailService.sendSupportEmail(
                        customer.getEmail(),
                        agent.getName(),
                        message,
                        ticketId
                );
            } catch (Exception e) {
                System.out.println("EMAIL ERROR: " + e.getMessage());
            }
        }

        return saved;
    }

    // ================= GET MESSAGES =================
    @Override
    public List<TicketMessage> getMessages(Long ticketId) {
        return messageRepo.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }

    @Override
    public Long extractUserIdFromToken(String token) {
        try {
            String[] chunks = token.split("\\.");
            String payload = new String(java.util.Base64.getDecoder().decode(chunks[1]));
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> map = mapper.readValue(payload, Map.class);

            return Long.valueOf(map.get("accno").toString());

        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }
}