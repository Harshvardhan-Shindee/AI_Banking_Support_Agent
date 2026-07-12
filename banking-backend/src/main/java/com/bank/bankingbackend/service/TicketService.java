package com.bank.bankingbackend.service;

import com.bank.bankingbackend.entity.SupportAgent;
import com.bank.bankingbackend.entity.Ticket;
import com.bank.bankingbackend.entity.TicketMessage;

import java.util.List;
import java.util.Map;

public interface TicketService {
    Ticket createTicket(Long userId, String issue);

    List<Ticket> getAllTickets();
    
    Map<String, String> resolveTicket(Long id);

    void autoEscalateTickets();

    Long getSeniorAgentId();

    List<Ticket> filterTickets(String status, String priority);

    Long getNextAvailableAgent();

    void sendTicketEmail(Ticket ticket, SupportAgent agent);

    TicketMessage replyToTicket(Long ticketId, String message, String sender);

    List<TicketMessage> getMessages(Long ticketId);

    Long extractUserIdFromToken(String token);
}
