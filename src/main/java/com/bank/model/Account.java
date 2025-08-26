package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    // Getter and Setter
    public User getManager() { return manager; }
    public void setManager(User manager) { this.manager = manager; }

    @Column(unique = true)
    private String accountNumber;

    @Min(value = 0, message = "Balance cannot be negative")
    private BigDecimal balance;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Add this enum inside the Account class or as a separate file
    public enum AccountStatus {
        ACTIVE, SUSPENDED, CLOSED
    }

    // Constructors
    public Account() {
        this.balance = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
    }

    public Account(String accountNumber, User user) {
        this();
        this.accountNumber = accountNumber;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // Business methods
    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(this.balance) > 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }
}