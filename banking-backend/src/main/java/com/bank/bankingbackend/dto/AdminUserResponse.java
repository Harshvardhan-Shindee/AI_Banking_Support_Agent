package com.bank.bankingbackend.dto;

public class AdminUserResponse {

    private Long accno;
    private String custname;
    private String acctype;
    private String email;
    private double balance;
    private String role;
    private String status;
    private String dp;

    // ✅ Default Constructor
    public AdminUserResponse() {
    }

    // ✅ Parameterized Constructor
    public AdminUserResponse(Long accno, String custname, String acctype,
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

    // ✅ GETTERS

    public Long getAccno() {
        return accno;
    }

    public String getCustname() {
        return custname;
    }

    public String getAcctype() {
        return acctype;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getDp() {
        return dp;
    }

    // ✅ SETTERS

    public void setAccno(Long accno) {
        this.accno = accno;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}