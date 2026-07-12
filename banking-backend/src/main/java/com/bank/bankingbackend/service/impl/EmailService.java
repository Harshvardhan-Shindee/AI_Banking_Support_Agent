package com.bank.bankingbackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // 🔥 HTML enable

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String buildTemplate(String name, String message, String status) {

        return """
        <div style="font-family: Arial; background:#f4f6f9; padding:20px;">
            <div style="max-width:500px; margin:auto; background:white; padding:20px; border-radius:10px;">

                <h2 style="color:#2f4f6f;">SecureBank</h2>

                <p>Dear <b>%s</b>,</p>

                <p>%s</p>

                <div style="
                    margin:20px 0;
                    padding:12px;
                    border-radius:6px;
                    background:%s;
                    color:white;
                    text-align:center;
                    font-weight:bold;
                ">
                    %s
                </div>

                <p style="font-size:12px; color:gray;">
                    This is an automated message. Do not reply.
                </p>

            </div>
        </div>
    """.formatted(
                name,
                message,
                getColor(status),
                status
        );
    }

    private String getColor(String status) {
        return switch (status) {
            case "APPROVED" -> "#2ecc71";
            case "REJECTED" -> "#e74c3c";
            case "BLOCKED" -> "#e67e22";
            case "UNBLOCKED" -> "#3498db";
            default -> "#2f4f6f";
        };
    }
}
