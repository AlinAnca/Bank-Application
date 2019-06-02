package com.bank.application.model;

import com.bank.application.util.Currency;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private User user;

    @NotNull
    @Size(min = 24, max = 24)
    @Pattern(regexp = "^RO")
    @Column(name = "account_number", length = 24)
    private String accountNumber;

    @PositiveOrZero
    @Column(name = "balance")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private Currency currency;

    @Column(name = "created_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdTime;

    @Column(name = "updated_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updatedTime;

    private Account() {
    }

    public Account(AccountBuilder accountBuilder) {
        this.user = accountBuilder.user;
        this.accountNumber = accountBuilder.accountNumber;
        this.balance = accountBuilder.balance;
        this.currency = accountBuilder.currency;
        this.updatedTime = accountBuilder.updatedTime;
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    @PrePersist
    void preInsert() {
        if (this.createdTime == null)
            this.createdTime = LocalDateTime.now();
        if (this.updatedTime == null)
            this.updatedTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public static class AccountBuilder {

        private User user;
        private String accountNumber;
        private BigDecimal balance;
        private Currency currency;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;

        public AccountBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public AccountBuilder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountBuilder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public AccountBuilder withCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public AccountBuilder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public AccountBuilder withUpdatedTime(LocalDateTime updatedTime) {
            this.updatedTime = updatedTime;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}
