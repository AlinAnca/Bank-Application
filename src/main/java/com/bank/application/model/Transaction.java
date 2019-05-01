package com.bank.application.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "to_account", length = 24)
    private String toAccount;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "details", length = 100, columnDefinition = "varchar(100) default '-'")
    private String details;

    @Transient
    @Column(name = "created_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction(Builder builder) {
        this.toAccount = builder.toAccount;
        this.balance = builder.balance;
        this.details = builder.details;
        this.createdTime = builder.createdTime;
        this.account = builder.account;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static class Builder {

        private String toAccount;
        private BigDecimal balance;
        private String details;
        private LocalDateTime createdTime;
        private Account account;

        public Builder toAccount(String toAccount) {
            this.toAccount = toAccount;
            return this;
        }

        public Builder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder withDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public Builder fromAccount(Account account) {
            this.account = account;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
