package com.bank.bankingbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_messages")
public class TicketMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ticketId;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String senderType; // USER or AGENT

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ================= GETTERS =================

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderType() {
        return senderType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ================= SETTERS =================

    public void setId(Long id) {
        this.id = id;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}