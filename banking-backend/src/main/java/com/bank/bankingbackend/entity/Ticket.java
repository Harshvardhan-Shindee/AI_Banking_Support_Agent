package com.bank.bankingbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketId; // Human readable ID

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String issue;

    private String status; // OPEN, IN_PROGRESS, RESOLVED

    private String priority; // LOW, MEDIUM, HIGH

    private Long assignedTo; // Admin/Agent ID

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ================= LIFECYCLE =================
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.ticketId = "TCK" + System.currentTimeMillis();
        this.status = "OPEN";
        this.priority = "MEDIUM";
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ================= GETTERS =================
    public Long getId() { return id; }
    public String getTicketId() { return ticketId; }
    public Long getUserId() { return userId; }
    public String getIssue() { return issue; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public Long getAssignedTo() { return assignedTo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ================= SETTERS =================
    public void setId(Long id) { this.id = id; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setIssue(String issue) { this.issue = issue; }
    public void setStatus(String status) { this.status = status; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}