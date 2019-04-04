package com.bank.application.util;

import com.bank.application.cache.AccountCache;
import com.bank.application.model.Account;
import com.bank.application.repository.DataCollection;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.logging.Logger;

public class PaymentValidation {
    private final static Logger logger = Logger.getLogger(AccountValidation.class.getName());

    public static void doTransfer(String username) {
        String accountFrom;
        String amount;
        String accountTo;
        Scanner keyboard = new Scanner(System.in);

        if (!checkAccountUniqueness(username)) {
            do {
                System.out.print("Insert Account from which you want to make a transfer: ");
                accountFrom = keyboard.next();
            } while (!checkAccountExistence(accountFrom, username));

            do {
                System.out.print("Insert Amount: ");
                amount = keyboard.next();
            } while (!checkAmount(amount, accountFrom));

            do {
                System.out.print("Insert Account to which you want to send the money: ");
                accountTo = keyboard.next();
            } while (!checkAccountExistence(accountTo, username) || checkAccountsEquality(accountFrom, accountTo));

            depositAmount(accountFrom, amount, accountTo);
        }
    }

    public static void depositAmount(String accountFrom, String amount, String accountTo) {
        for (Account account : AccountCache.getAccountsFromFile()) {
            if (account.getAccountNumber().equals(accountFrom)) {
                BigDecimal newBalance = account.getBalance().subtract(new BigDecimal(amount));
                account.setBalance(newBalance);
            }
            if (account.getAccountNumber().equals(accountTo)) {
                BigDecimal newBalance = account.getBalance().add(new BigDecimal(amount));
                account.setBalance(newBalance);
            }
        }
    }

    private static boolean checkAccountsEquality(String accountFrom, String accountTo) {
        if (accountFrom.equals(accountTo)) {
            logger.warning("This is the transfer account. \nPlease try again..");
            return true;
        }
        return false;
    }

    private static boolean checkAccountUniqueness(String username) {
        if (DataCollection.getAccountsForUser(username).size() == 1) {
            logger.warning("You are not able to make a transfer. Only one account founded!");
            return true;
        }
        return false;
    }

    private static boolean checkAccountExistence(String accountNumber, String username) {
        for (Account account : DataCollection.getAccountsForUser(username)) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return true;
            }
        }
        logger.warning("Account does not exist. \nPlease try again..");
        return false;
    }

    private static boolean checkAmount(String amountToTranfer, String accountFrom) {
        double amount = Double.valueOf(amountToTranfer);
        double amountAv = getAvailableAmount(accountFrom).doubleValue();

        if (amount < 0 || amount > amountAv) {
            logger.warning("Invalid amount.");
            return false;
        }
        return true;
    }

    private static BigDecimal getAvailableAmount(String accountFrom) {
        BigDecimal availableAmount = null;
        for (Account account : AccountCache.getAccountsFromFile()) {
            if (account.getAccountNumber().equals(accountFrom)) {
                availableAmount = account.getBalance();
            }
        }
        return availableAmount;
    }
}
