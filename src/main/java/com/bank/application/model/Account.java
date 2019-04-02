package com.bank.application.model;

import com.bank.application.util.AccountType;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private String accountNumber;
    private String username;
    private BigDecimal balance;
    private AccountType accountType;

    public Account(String accountNumber, String username, BigDecimal balance, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = balance;
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber) &&
                Objects.equals(username, account.username) &&
                accountType == account.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, username, balance, accountType);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                ", accountType=" + accountType +
                '}';
    }
}
