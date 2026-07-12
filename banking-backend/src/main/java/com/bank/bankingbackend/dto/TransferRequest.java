package com.bank.bankingbackend.dto;

public class TransferRequest {

    private Long senderAccno;
    private Long receiverAccno;
    private double amount;

    public TransferRequest() {}

    public TransferRequest(Long senderAccno, Long receiverAccno, double amount) {
        this.senderAccno = senderAccno;
        this.receiverAccno = receiverAccno;
        this.amount = amount;
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
}