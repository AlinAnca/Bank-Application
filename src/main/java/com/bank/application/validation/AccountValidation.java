package com.bank.application.validation;

import com.bank.application.model.Account;
import com.bank.application.repository.AccountCollection;
import com.bank.application.util.Currency;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * AccountValidation is the class which validates the information regarding the new account which user wants to create.
 */
public class AccountValidation {
    private static final Logger LOGGER = Logger.getLogger(AccountValidation.class.getName());

    /**
     * Reads data from user's application until it's valid.
     * @param username  the username for logged User
     * @return a new Account
     */
    public static Account getAccount(String username) {
        String accountNumber;
        double amount;
        String accountType;
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Insert Account Number: ");
            accountNumber = keyboard.next();
        } while (!checkAccountNumber(accountNumber));

        do {
            amount = getAmount();
        } while (!checkAmount(amount));

        do {
            System.out.print("Insert Account Type: ");
            accountType = keyboard.next();
            accountType = accountType.toUpperCase().trim();
        } while (!checkAccountType(accountType));

        return new Account(accountNumber, username, new BigDecimal(amount), Currency.valueOf(accountType));
    }

    /**
     * Gets the amount from user.
     * Only allows decimal numbers, otherwise catches {@link InputMismatchException}.
     * @return the amount
     */
    private static double getAmount() {
        Scanner keyboard = new Scanner(System.in);
        double amount = -1;
        System.out.print("Insert Amount: ");
        try {
            amount = keyboard.nextDouble();
        } catch (InputMismatchException e) {
            LOGGER.finer(e.getMessage());
        }
        return amount;
    }

    /**
     * Checks if given amount is valid.
     * @param amount  the amount to be validated
     * @return <code>true</code> if the amount is positive number;
     *         <code>false</code> otherwise
     */
    private static boolean checkAmount(double amount) {
        if (amount < 0) {
            LOGGER.warning("Invalid amount.");
            return false;
        }
        return true;
    }

    /**
     * Checks if account type is RON or EUR.
     * @param accountType the account type to be validated
     * @return <code>true</code> if the account type is valid;
     *         <code>false</code> otherwise
     */
    private static boolean checkAccountType(String accountType) {
        String type = accountType.toUpperCase().trim();
        if (type.equals("RON") || type.equals("EUR")) {
            return true;
        }
        LOGGER.warning("Invalid type.");
        return false;
    }

    /**
     * Checks if given account number starts with RO and has a valid length of 24 characters.
     * @param accountNumber  the account number to be validated
     * @return <code>true</code> if the account number is valid;
     *         <code>false</code> otherwise
     */
    private static boolean checkAccountNumber(String accountNumber) {
        if (!accountNumber.toUpperCase().startsWith("RO")) {
            LOGGER.warning("Invalid account number: " + accountNumber + ". It should start with 'RO'");
            return false;
        } else if (accountNumber.length() != 24) {
            LOGGER.warning("Invalid account number: " + accountNumber + ". Account number length should be 24.");
            return false;
        }
        return checkAccountNumberUniqueness(accountNumber);
    }

    /**
     * Checks if the account number is unique.
     * @param accountNumber the account number to be validated
     * @return <code>true</code> if the account is unique;
     *         <code>false</code> otherwise
     */
    private static boolean checkAccountNumberUniqueness(String accountNumber) {
        for (Account account : AccountCollection.getAccounts()) {
            if (account.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                LOGGER.warning("Account number already exists!\nPlease try again.. ");
                return false;
            }
        }
        return true;
    }
}
