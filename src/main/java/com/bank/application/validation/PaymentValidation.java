package com.bank.application.validation;

import com.bank.application.model.Account;
import com.bank.application.util.Currency;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PaymentValidation {
    private final static Logger logger = Logger.getLogger(AccountValidation.class.getName());

    public static void transferMoney(List<Account> accountList) {
        Scanner keyboard = new Scanner(System.in);
        String accountFrom;
        double amount;
        String accountTo;

        if (!checkAccountUniquenessForEachCurrency(accountList)) {
            do {
                System.out.print("Insert Account from which you want to make a transfer: ");
                accountFrom = keyboard.next();
            } while (!checkAccountExistence(accountList, accountFrom) || !checkAccountFrom(accountList, accountFrom));

            do {
                amount = getAmount();
            } while (!checkAmount(accountList, amount, accountFrom));

            do {
                System.out.print("Insert Account to which you want to send the money: ");
                accountTo = keyboard.next();
            } while (!checkAccountExistence(accountList, accountTo) || !checkAccountTo(accountList, accountFrom, accountTo));

            depositAmount(accountList, accountFrom, amount, accountTo);
        }
    }

    private static double getAmount() {
        Scanner keyboard = new Scanner(System.in);
        double amount = 0;
        System.out.print("Insert Amount: ");
        try {
            amount = keyboard.nextDouble();
        } catch (InputMismatchException e) {
            logger.fine(e.getMessage());
        }
        return amount;
    }

    private static void depositAmount(List<Account> accountList, String accountFrom, double amount, String accountTo) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountFrom)) {
                BigDecimal newBalance = account.getBalance().subtract(new BigDecimal(amount));
                account.setBalance(newBalance);
            }
            if (account.getAccountNumber().equals(accountTo)) {
                BigDecimal newBalance = account.getBalance().add(new BigDecimal(amount));
                account.setBalance(newBalance);
            }
        }
        logger.info("Transfer has been processed successfully.");
    }

    private static boolean checkAccountTo(List<Account> accountList, String accountFrom, String accountTo) {
        if (!getAccountType(accountList, accountFrom).equals(getAccountType(accountList, accountTo))) {
            logger.warning("Different account types! \nPlease try again..");
            return false;
        }
        if (accountFrom.equals(accountTo)) {
            logger.warning("This is the transfer account. \nPlease try again..");
            return false;
        }
        return true;
    }

    private static boolean checkAccountFrom(List<Account> accountList, String accountFrom) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountFrom) && account.getBalance().equals(new BigDecimal(0))) {
                logger.warning("You don't have enough money in your account. \nPlease try again..");
                return false;
            }
        }
        Map<Currency, Long> currencyMap = getNumbersOfAccountsByCurrency(accountList);
        for (Map.Entry<Currency, Long> entry : currencyMap.entrySet()) {
            if (entry.getKey().equals(getAccountType(accountList, accountFrom).get()) && entry.getValue() == 1) {
                logger.warning("You have only one account of type " + entry.getKey() + "! Please try again..");
                return false;
            }
        }
        return true;
    }

    private static Optional<Currency> getAccountType(List<Account> accountList, String account) {
        for (Account a : accountList) {
            if (a.getAccountNumber().equals(account)) {
                return Optional.of(a.getCurrency());
            }
        }
        return Optional.empty();
    }

    private static boolean checkAccountUniquenessForEachCurrency(List<Account> accountList) {
        if (checkAccountUniqueness(accountList)) {
            return true;
        }
        if (!checkAccountUniqueness(accountList)) {
            Map<Currency, Long> currencyMap = getNumbersOfAccountsByCurrency(accountList);
            if (currencyMap.get(Currency.EUR) == 1 && currencyMap.get(Currency.RON) == 1) {
                logger.warning("You are not able to make a transfer. You have only one account for each type, EUR and RON!");
                return true;
            }
        }
        return false;
    }

    private static Map<Currency, Long> getNumbersOfAccountsByCurrency(List<Account> accountList) {
        return accountList.stream()
                .collect(Collectors.groupingBy(a -> a.getCurrency(), Collectors.counting()));
    }

    private static boolean checkAccountUniqueness(List<Account> accountList) {
        if (accountList.size() == 1) {
            logger.warning("You are not able to make a transfer. Only one account founded!");
            return true;
        }
        return false;
    }

    private static boolean checkAccountExistence(List<Account> accountList, String accountNumber) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return true;
            }
        }
        logger.warning("Account does not exist! \nPlease try again..");
        return false;
    }

    private static boolean checkAmount(List<Account> accountList, double amount, String accountFrom) {
        double amountAv = getAvailableAmount(accountList, accountFrom).doubleValue();
        if (amount <= 0 || amount > amountAv) {
            logger.warning("Invalid amount.");
            return false;
        }
        return true;
    }

    private static BigDecimal getAvailableAmount(List<Account> accountList, String accountFrom) {
        BigDecimal availableAmount = null;
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountFrom)) {
                availableAmount = account.getBalance();
            }
        }
        return availableAmount;
    }
}
