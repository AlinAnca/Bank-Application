package com.bank.application.model;

import com.bank.application.util.Currency;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Account entity.
 * Contains:
 * <ul>
 *     <li>account number</li>
 *     <li>username</li>
 *     <li>balance</li>
 *     <li>currency of type {@link Currency}</li>
 * </ul>
 */
public class Account {
    private String accountNumber;
    private String username;
    private BigDecimal balance;
    private Currency currency;

    public Account(String accountNumber, String username, BigDecimal balance, Currency currency) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = balance;
        this.currency = currency;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber) &&
                Objects.equals(username, account.username) &&
                currency == account.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, username, balance, currency);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                ", accountType=" + currency +
                '}';
    }
}
