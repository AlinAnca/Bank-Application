package com.bank.application.util;

import com.bank.application.cache.AccountCache;
import com.bank.application.model.Account;
import com.bank.application.repository.DataCollection;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PaymentValidation {
    private final static Logger logger = Logger.getLogger(AccountValidation.class.getName());

    public static void doTransfer(String username) {
        String accountFrom;
        String amount;
        String accountTo;
        Scanner keyboard = new Scanner(System.in);

        if (!checkAccountUniquenessForEachCurrency(username)) {
            do {
                System.out.print("Insert Account from which you want to make a transfer: ");
                accountFrom = keyboard.next();
            } while (!checkAccountExistence(username, accountFrom) || !checkAccountFrom(username, accountFrom));

            do {
                System.out.print("Insert Amount: ");
                amount = keyboard.next();
            } while (!checkAmount(amount, accountFrom));

            do {
                System.out.print("Insert Account to which you want to send the money: ");
                accountTo = keyboard.next();
            } while (!checkAccountExistence(username, accountTo) || !checkAccountTo(accountFrom, accountTo));

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

    private static boolean checkAccountTo(String accountFrom, String accountTo) {
        if (!getAccountType(accountFrom).equals(getAccountType(accountTo))) {
            logger.warning("Different account types! \nPlease try again..");
            return false;
        }
        if (accountFrom.equals(accountTo)) {
            logger.warning("This is the transfer account. \nPlease try again..");
            return false;
        }
        return true;
    }

    private static boolean checkAccountFrom(String username, String accountFrom) {
        for (Account account : AccountCache.getAccountsFromFile()) {
            if (account.getAccountNumber().equals(accountFrom) && account.getBalance().equals(new BigDecimal(0))) {
                logger.warning("You don't have enough money in your account. \nPlease try again..");
                return false;
            }
        }
        Map<Currency, Long> currencyMap = getNumbersOfAccountsByCurrency(username);
        for (Map.Entry<Currency, Long> entry : currencyMap.entrySet()) {
            if (entry.getKey().equals(getAccountType(accountFrom)) && entry.getValue() == 1) {
                logger.warning("You have only one account of type " + entry.getKey() + "! Please try again..");
                return false;
            }
        }
        return true;
    }

    private static Currency getAccountType(String account) {
        for (Account a : AccountCache.getAccountsFromFile()) {
            if (a.getAccountNumber().equals(account)) {
                return a.getAccountType();
            }
        }
        return null;
    }

    private static boolean checkAccountUniquenessForEachCurrency(String username) {
        if (checkAccountUniqueness(username)) {
            return true;
        }
        if (!checkAccountUniqueness(username)) {
            Map<Currency, Long> currencyMap = getNumbersOfAccountsByCurrency(username);
            for (Map.Entry<Currency, Long> entry : currencyMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            if (currencyMap.get(Currency.EUR) == 1 && currencyMap.get(Currency.RON) == 1) {
                logger.warning("You are not able to make a transfer. You have only one account for each type, EUR and RON!");
                return true;
            }
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

    private static Map<Currency, Long> getNumbersOfAccountsByCurrency(String username) {
        return DataCollection.getAccountsForUser(username)
                .stream()
                .collect(Collectors.groupingBy(a -> a.getAccountType(), Collectors.counting()));
    }

    private static boolean checkAccountExistence(String username, String accountNumber) {
        for (Account account : DataCollection.getAccountsForUser(username)) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return true;
            }
        }
        logger.warning("Account does not exist! \nPlease try again..");
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
