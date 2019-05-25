package com.bank.application.model.DTO;

import com.bank.application.model.User;
import com.bank.application.util.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountDTO {
    private User user;
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;
    private LocalDateTime createdTime;

    public AccountDTO(AccountDTOBuilder builder) {
        this.user = builder.user;
        this.accountNumber = builder.accountNumber;
        this.balance = builder.balance;
        this.currency = builder.currency;
        this.createdTime = builder.createdTime;
    }

    public static AccountDTOBuilder builder() {
        return new AccountDTOBuilder();
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

    public static class AccountDTOBuilder {

        private User user;
        private String accountNumber;
        private BigDecimal balance;
        private Currency currency;
        private LocalDateTime createdTime;

        public AccountDTOBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public AccountDTOBuilder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountDTOBuilder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public AccountDTOBuilder withCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public AccountDTOBuilder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public AccountDTO build() {
            return new AccountDTO(this);
        }
    }
}
