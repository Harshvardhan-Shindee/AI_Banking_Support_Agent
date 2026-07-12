package com.bank.bankingbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accno;
    private String custname;
    private String acctype;
    @Column(unique = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String dp;
    private double balance ;
    private String role = "CUSTOMER";
    private String status = "PENDING";
    public Customer() {
        super();
    }
    public Customer(Long accno, String custname, String acctype, String email, String password, String dp,
                    double balance, String role, String status) {
        super();
        this.accno = accno;
        this.custname = custname;
        this.acctype = acctype;
        this.email = email;
        this.password = password;
        this.dp = dp;
        this.balance = balance;
        this.role = role;
        this.status = status;
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getDp() {
        return dp;
    }
    public void setDp(String dp) {
        this.dp = dp;
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
    @Override
    public String toString() {
        return "Customer [accno=" + accno + ", custname=" + custname + ", acctype=" + acctype + ", email=" + email
                + ", password=" + password + ", dp=" + dp + ", balance=" + balance + ", role=" + role + ", status="
                + status + "]";
    }

}