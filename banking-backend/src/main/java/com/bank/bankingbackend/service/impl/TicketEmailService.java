package com.bank.bankingbackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class TicketEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSupportEmail(
            String customerEmail,
            String agentName,
            String issue,
            Long ticketId
    ) {

        String subject = "Support Ticket Created - ID " + ticketId;

        String body = String.format("""
                Dear Customer,
                
                Thank you for contacting our support team.
                
                We have received your request:
                
                "%s"
                
                🆔 Ticket ID: %d
                👤 Assigned Agent: %s
                
                Our team is actively working on your issue and will resolve it within 24 hours.
                
                If you have additional details, you can reply to this email or continue in chat.
                
                Best regards,
                %s
                Customer Support Team
                """,
                issue,
                ticketId,
                agentName,
                agentName
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("support@bank.com");
            message.setTo(customerEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("EMAIL FAILED: " + e.getMessage());
        }
    }
}