package com.bank.application.validation;

import com.bank.application.model.Account;
import com.bank.application.util.Currency;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * PaymentValidation is the class which ensures that user makes a valid transfer.
 */
public class PaymentValidation {
    private static final Logger LOGGER = Logger.getLogger(AccountValidation.class.getName());

    /**
     * Makes valid transfers between accounts of the same type of current user.
     * This is possible only if user has at least two accounts of same type.
     * @param accountList the list of accounts of current user
     */
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

    /**
     * Deposits the amount from an account to another from the current user's account list.
     * @param accountList  the list of accounts of current user;
     * @param accountFrom  the account from which is made the transfer
     * @param amount       the amount to transfer
     * @param accountTo    the account to which is made the transfer
     */
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
        LOGGER.info("Transfer has been processed successfully.");
    }

    /**
     * Gets the amount to be transferred from user.
     * Only allows decimal numbers, otherwise catches {@link InputMismatchException}.
     * @return the amount
     */
    private static double getAmount() {
        Scanner keyboard = new Scanner(System.in);
        double amount = 0;
        System.out.print("Insert Amount: ");
        try {
            amount = keyboard.nextDouble();
        } catch (InputMismatchException e) {
            LOGGER.fine(e.getMessage());
        }
        return amount;
    }

    /**
     * Checks if the amount to be transferred is valid.
     * @param accountList  the list of accounts of current user;
     * @param amount       the amount to check;
     * @param accountFrom  the account from which is made the transfer
     * @return <code>true</code> if the amount is valid;
     *         <code>false</code> otherwise
     */
    private static boolean checkAmount(List<Account> accountList, double amount, String accountFrom) {
        double amountAv = getAvailableAmount(accountList, accountFrom).doubleValue();
        if (amount <= 0 || amount > amountAv) {
            LOGGER.warning("Invalid amount.");
            return false;
        }
        return true;
    }

    /**
     * Gets available amount of the account from which the transfer is made.
     * @param accountList  the list of accounts of current user
     * @param accountFrom  the account number from which is made the transfer
     * @return the available amount from account
     */
    private static BigDecimal getAvailableAmount(List<Account> accountList, String accountFrom) {
        BigDecimal availableAmount = null;
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountFrom)) {
                availableAmount = account.getBalance();
            }
        }
        return availableAmount;
    }

    /**
     * Checks account to which is made the transfer.
     * It should have the same type as account from which is made the transfer.
     * Can't be the same account.
     * @param accountList the list of accounts of current user
     * @param accountFrom the account form which is made the transfer
     * @param accountTo   the account to which is made the transfer
     * @return <code>true</code> if the account is valid;
     *         <code>false</code> otherwise
     */
    private static boolean checkAccountTo(List<Account> accountList, String accountFrom, String accountTo) {
        if (!getAccountType(accountList, accountFrom).equals(getAccountType(accountList, accountTo))) {
            LOGGER.warning("Different account types! \nPlease try again..");
            return false;
        }
        if (accountFrom.equals(accountTo)) {
            LOGGER.warning("This is the transfer account. \nPlease try again..");
            return false;
        }
        return true;
    }

    /**
     * Checks account from which is made the transfer.
     * The account must not be empty, so the transfer is valid.
     * User should have at least two accounts of the same type as inserted account.
     * @param accountList the list of accounts of current user
     * @param accountFrom the account form which is made the transfer
     * @return <code>true</code> if the account is valid;
     *         <code>false</code> otherwise
     */
    private static boolean checkAccountFrom(List<Account> accountList, String accountFrom) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountFrom) && account.getBalance().equals(new BigDecimal(0))) {
                LOGGER.warning("You don't have enough money in your account. \nPlease try again..");
                return false;
            }
        }
        Map<Currency, Long> currencyMap = getNumberOfAccountsByCurrency(accountList);
        for (Map.Entry<Currency, Long> entry : currencyMap.entrySet()) {
            if (entry.getKey().equals(getAccountType(accountList, accountFrom).get()) && entry.getValue() == 1) {
                LOGGER.warning("You have only one account of type " + entry.getKey() + "! Please try again..");
                return false;
            }
        }
        return true;
    }

    /**
     * Gets account's type.
     * @param accountList the list of accounts of current user
     * @param account     the account
     * @return <code>Currency</code> if found;
     *         nothing otherwise.
     */
    private static Optional<Currency> getAccountType(List<Account> accountList, String account) {
        for (Account a : accountList) {
            if (a.getAccountNumber().equals(account)) {
                return Optional.of(a.getCurrency());
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if current user has more than one account of the same type.
     * @param accountList  the list of accounts of current user
     * @return <code>true</code> if the account is unique for each type;
     *         <code>false</code> otherwise
     */
    private static boolean checkAccountUniquenessForEachCurrency(List<Account> accountList) {
        if (checkAccountUniqueness(accountList)) {
            return true;
        }
        if (!checkAccountUniqueness(accountList)) {
            Map<Currency, Long> currencyMap = getNumberOfAccountsByCurrency(accountList);
            if (currencyMap.get(Currency.EUR) == 1 && currencyMap.get(Currency.RON) == 1) {
                LOGGER.warning("You are not able to make a transfer. You have only one account for each type, EUR and RON!");
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the number of accounts for each type.
     * @param accountList  the list of accounts of current user
     * @return the map containing each type, RON and EUR, and accounts number for each of them
     */
    private static Map<Currency, Long> getNumberOfAccountsByCurrency(List<Account> accountList) {
        return accountList.stream()
                .collect(Collectors.groupingBy(Account::getCurrency, Collectors.counting()));
    }

    /**
     * Checks the uniqueness of account from user's account list,
     * @param accountList the accounts of current user
     * @return <code>true</code> if the account is unique;
     *         <code>false</code> otherwise
     */
    private static boolean checkAccountUniqueness(List<Account> accountList) {
        if (accountList.size() == 1) {
            LOGGER.warning("You are not able to make a transfer. Only one account founded!");
            return true;
        }
        return false;
    }

    /**
     * Checks the existence of the account.
     * @param accountList    the accounts of current user
     * @param accountNumber  the account number
     * @return <code>true</code> if the account exists;
     *         <code>false</code> otherwise
     */
    private static boolean checkAccountExistence(List<Account> accountList, String accountNumber) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return true;
            }
        }
        LOGGER.warning("Account does not exist! \nPlease try again..");
        return false;
    }
}
