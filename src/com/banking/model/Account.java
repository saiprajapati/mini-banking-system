package com.banking.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
    private String accountNumber;
    private int userId;
    private String accountType;   // SAVINGS, CURRENT, FIXED_DEPOSIT
    private BigDecimal balance;
    private String status;        // ACTIVE, INACTIVE, FROZEN
    private Timestamp createdAt;
    private String ownerName;     // joined from users table

    public Account() {}

    public Account(String accountNumber, int userId, String accountType, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.status = "ACTIVE";
    }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    @Override
    public String toString() {
        return accountNumber + " | " + accountType + " | ₹" + balance + " | " + status;
    }
}
