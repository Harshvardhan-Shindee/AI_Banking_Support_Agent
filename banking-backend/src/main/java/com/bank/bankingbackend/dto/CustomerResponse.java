package com.bank.bankingbackend.dto;

public class CustomerResponse {

    private Long accno;
    private String custname;
    private String acctype;
    private String email;
    private double balance;
    private String role;
    private String status;
    private String dp;   // 🔥 profile picture URL

    public CustomerResponse() {
    }

    public CustomerResponse(Long accno, String custname, String acctype,
                            String email, double balance,
                            String role, String status, String dp) {
        this.accno = accno;
        this.custname = custname;
        this.acctype = acctype;
        this.email = email;
        this.balance = balance;
        this.role = role;
        this.status = status;
        this.dp = dp;
    }

    public Long getAccno() {
        return accno;
    }

    public void setAccno(Long accno) {
        this.accno = accno;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}