package com.bank.bankingbackend.dto;

import java.util.List;

public class AdminStatsResponse {

    private long totalUsers;
    private long activeUsers;
    private long blockedUsers;
    private long pendingUsers;
    private long rejectedUsers;
    private long totalTransactions;
    private long todayTransactions;
    private double totalBalance;

    private List<TransactionResponse> recentTransactions;

    // ✅ Default constructor
    public AdminStatsResponse() {
    }

    // ✅ GETTERS

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public long getBlockedUsers() {
        return blockedUsers;
    }

    public long getPendingUsers() {
        return pendingUsers;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public long getTodayTransactions() {
        return todayTransactions;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public List<TransactionResponse> getRecentTransactions() {
        return recentTransactions;
    }

    // ✅ SETTERS

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public void setBlockedUsers(long blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public void setPendingUsers(long pendingUsers) {
        this.pendingUsers = pendingUsers;
    }

    public long getRejectedUsers() {
        return rejectedUsers;
    }

    public void setRejectedUsers(long rejectedUsers) {
        this.rejectedUsers = rejectedUsers;
    }

    public void setTotalTransactions(long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public void setTodayTransactions(long todayTransactions) {
        this.todayTransactions = todayTransactions;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public void setRecentTransactions(List<TransactionResponse> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }
}