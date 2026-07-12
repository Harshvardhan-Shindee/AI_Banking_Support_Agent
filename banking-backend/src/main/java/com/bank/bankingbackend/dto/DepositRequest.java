package com.bank.bankingbackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DepositRequest {

    @NotNull
    private Long accno;
    @Min(1)
    private double amount;

    public DepositRequest() {
    }

    public DepositRequest(Long accno, double amount) {
        this.accno = accno;
        this.amount = amount;
    }

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