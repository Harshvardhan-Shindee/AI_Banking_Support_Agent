package com.bank.bankingbackend.dto;

public class WithdrawRequest {

    private Long accno;
    private double amount;

    public Long getAccno() {
        return accno;
    }

    public void setAccno(Long accno) {
        this.accno = accno;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}