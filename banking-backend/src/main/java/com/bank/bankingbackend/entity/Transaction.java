package com.bank.bankingbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transId;
    private Long senderAccno;
    private Long receiverAccno;
    private double amount;
    private String type;
    private LocalDateTime dateTime;

    public Transaction() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Transaction(Long transId, Long senderAccno, Long receiverAccno, double amount, String type,
                       LocalDateTime dateTime) {
        super();
        this.transId = transId;
        this.senderAccno = senderAccno;
        this.receiverAccno = receiverAccno;
        this.amount = amount;
        this.type = type;
        this.dateTime = dateTime;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Long getSenderAccno() {
        return senderAccno;
    }

    public void setSenderAccno(Long senderAccno) {
        this.senderAccno = senderAccno;
    }

    public Long getReceiverAccno() {
        return receiverAccno;
    }

    public void setReceiverAccno(Long receiverAccno) {
        this.receiverAccno = receiverAccno;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Transaction [transId=" + transId + ", senderAccno=" + senderAccno + ", receiverAccno=" + receiverAccno
                + ", amount=" + amount + ", type=" + type + ", dateTime=" + dateTime + "]";
    }
}