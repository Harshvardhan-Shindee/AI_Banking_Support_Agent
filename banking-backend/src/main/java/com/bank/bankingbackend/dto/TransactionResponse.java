package com.bank.bankingbackend.dto;

public class TransactionResponse {

    private Long transId;
    private String type;
    private double amount;
    private String dateTime;

    private Long senderAccno;
    private Long receiverAccno;

    // constructor
    public TransactionResponse() {}

    public TransactionResponse(Long transId, String type, double amount,
                               String dateTime,
                               Long senderAccno, Long receiverAccno) {
        this.transId = transId;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
        this.senderAccno = senderAccno;
        this.receiverAccno = receiverAccno;
    }

    // getters & setters

    public Long getTransId() { return transId; }
    public void setTransId(Long transId) { this.transId = transId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public Long getSenderAccno() { return senderAccno; }
    public void setSenderAccno(Long senderAccno) { this.senderAccno = senderAccno; }

    public Long getReceiverAccno() { return receiverAccno; }
    public void setReceiverAccno(Long receiverAccno) { this.receiverAccno = receiverAccno; }
}