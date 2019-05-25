package com.bank.application.model.DTO;

import com.bank.application.util.Currency;

import java.math.BigDecimal;

public class AccountRequestDTO {
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;

    public AccountRequestDTO(AccountRequestDTOBuilder builder) {
        this.accountNumber = builder.accountNumber;
        this.balance = builder.balance;
        this.currency = builder.currency;
    }

    public AccountRequestDTO() {
    }

    public static AccountRequestDTOBuilder builder() {
        return new AccountRequestDTOBuilder();
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

    public static class AccountRequestDTOBuilder {

        private String accountNumber;
        private BigDecimal balance;
        private Currency currency;

        public AccountRequestDTOBuilder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountRequestDTOBuilder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public AccountRequestDTOBuilder withCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public AccountRequestDTO build() {
            return new AccountRequestDTO(this);
        }
    }
}