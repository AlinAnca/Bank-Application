package com.bank.application.validation;

import com.bank.application.model.Account;
import com.bank.application.model.Transaction;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.repository.TransactionRepository;
import com.bank.application.util.Currency;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * PaymentValidation is the class which ensures that user makes a valid transfer.
 */
public class PaymentValidation {
    private static final Logger LOGGER = Logger.getLogger(PaymentValidation.class.getName());

    /**
     * Makes valid transfers between accounts of the same type of current user.
     * This is possible only if user has at least two accounts of same type.
     */
    public static void transferMoney(User user) {
        Scanner keyboard = new Scanner(System.in);
        String accountFrom;
        double amount;
        String accountTo;
        String details;

        if (!checkAccountUniquenessForEachCurrency()) {
            do {
                System.out.print("Insert Account from which you want to make a transfer: ");
                accountFrom = keyboard.next();
            } while (!checkAccountFromExistence(user, accountFrom) || !checkAccountFrom(user, accountFrom));

            do {
                amount = getAmount();
            } while (!checkAmount(user, amount, accountFrom));

            do {
                System.out.print("Insert Account to which you want to send the money: ");
                accountTo = keyboard.next();
            } while (!checkAccountToExistence(accountTo) || !checkAccountTo(accountFrom, accountTo));

            System.out.print("Description: ");
            details = new Scanner(System.in).nextLine();

            Account account = depositAmount(accountFrom, amount, accountTo);
            Transaction transaction = Transaction.builder()
                    .toAccount(accountTo)
                    .withBalance(new BigDecimal(amount))
                    .fromAccount(account)
                    .withDetails(details)
                    .build();
            TransactionRepository.addTransaction(transaction);
        }
    }

    /**
     * Deposits the amount from an account to another from the current user's account list.
     *
     * @param accountNumberFrom the account from which is made the transfer
     * @param amount            the amount to transfer
     * @param accountNumberTo   the account to which is made the transfer
     */
    private static Account depositAmount(String accountNumberFrom, double amount, String accountNumberTo) {
        Account fromAccount = null;
        for (Account account : AccountRepository.getAccounts().get()) {
            if (account.getAccountNumber().equals(accountNumberFrom)) {
                fromAccount = account;

                BigDecimal newBalance = account.getBalance().subtract(new BigDecimal(amount));
                AccountRepository.updateAccountBalance(account.getId(), newBalance);
            }
            if (account.getAccountNumber().equals(accountNumberTo)) {
                BigDecimal newBalance = account.getBalance().add(new BigDecimal(amount));
                AccountRepository.updateAccountBalance(account.getId(), newBalance);
            }
        }
        LOGGER.info("Transfer has been processed successfully.");
        return fromAccount;
    }

    /**
     * Gets the amount to be transferred from user.
     * Only allows decimal numbers, otherwise catches {@link InputMismatchException}.
     *
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
     *
     * @param user        the current user;
     * @param amount      the amount to check;
     * @param accountFrom the account from which is made the transfer
     * @return <code>true</code> if the amount is valid;
     * <code>false</code> otherwise
     */
    private static boolean checkAmount(User user, double amount, String accountFrom) {
        double amountAv = getAvailableAmount(user, accountFrom).doubleValue();
        if (amount <= 0 || amount > amountAv) {
            LOGGER.warning("Invalid amount.");
            return false;
        }
        return true;
    }

    /**
     * Gets available amount of the account from which the transfer is made.
     *
     * @param user        the current user
     * @param accountFrom the account number from which is made the transfer
     * @return the available amount from account
     */
    private static BigDecimal getAvailableAmount(User user, String accountFrom) {
        BigDecimal availableAmount = null;
        for (Account account : AccountRepository.getAccountsFor(user)) {
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
     *
     * @param accountFrom the account form which is made the transfer
     * @param accountTo   the account to which is made the transfer
     * @return <code>true</code> if the account is valid;
     * <code>false</code> otherwise
     */
    private static boolean checkAccountTo(String accountFrom, String accountTo) {
        if (!getAccountType(accountFrom).equals(getAccountType(accountTo))) {
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
     *
     * @param user        the current user
     * @param accountFrom the account form which is made the transfer
     * @return <code>true</code> if the account is valid;
     * <code>false</code> otherwise
     */
    private static boolean checkAccountFrom(User user, String accountFrom) {
        for (Account account : AccountRepository.getAccountsFor(user)) {
            if (account.getAccountNumber().equals(accountFrom) && account.getBalance().equals(new BigDecimal(0))) {
                LOGGER.warning("You don't have enough money in your account. \nPlease try again..");
                return false;
            }
        }
        Map<Currency, Long> currencyMap = getNumberOfAccountsByCurrency();
        for (Map.Entry<Currency, Long> entry : currencyMap.entrySet()) {
            if (entry.getKey().equals(getAccountType(accountFrom).get()) && entry.getValue() == 1) {
                LOGGER.warning("You have only one account of type " + entry.getKey() + "! Please try again..");
                return false;
            }
        }
        return true;
    }

    /**
     * Gets account's type.
     *
     * @param account the account
     * @return <code>Currency</code> if found;
     * nothing otherwise.
     */
    private static Optional<Currency> getAccountType(String account) {
        for (Account a : AccountRepository.getAccounts().get()) {
            if (a.getAccountNumber().equals(account)) {
                return Optional.of(a.getCurrency());
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if current user has more than one account of the same type.
     *
     * @return <code>true</code> if the account is unique for each type;
     * <code>false</code> otherwise
     */
    private static boolean checkAccountUniquenessForEachCurrency() {
        if (checkAccountUniqueness()) {
            return true;
        }
        if (!checkAccountUniqueness()) {
            Map<Currency, Long> currencyMap = getNumberOfAccountsByCurrency();
            if (currencyMap.get(Currency.EUR) == 1 && currencyMap.get(Currency.RON) == 1) {
                LOGGER.warning("You are not able to make a transfer. You have only one account for each type, EUR and RON!");
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the number of accounts for each type.
     *
     * @return the map containing each type, RON and EUR, and accounts number for each of them
     */
    private static Map<Currency, Long> getNumberOfAccountsByCurrency() {
        return AccountRepository.getAccounts().get().stream()
                .collect(Collectors.groupingBy(Account::getCurrency, Collectors.counting()));
    }

    /**
     * Checks the uniqueness of account from repository.
     *
     * @return <code>true</code> if the account is unique;
     * <code>false</code> otherwise
     */
    private static boolean checkAccountUniqueness() {
        if (AccountRepository.getAccounts().get().size() == 1) {
            LOGGER.warning("You are not able to make a transfer. Only one account founded!");
            return true;
        }
        return false;
    }

    /**
     * Checks the existence of the account.
     *
     * @param accountFrom the account form which is made the transfer
     * @return <code>true</code> if the account exists;
     * <code>false</code> otherwise
     */
    private static boolean checkAccountFromExistence(User user, String accountFrom) {
        for (Account account : AccountRepository.getAccountsFor(user)) {
            if (account.getAccountNumber().equals(accountFrom)) {
                return true;
            }
        }
        LOGGER.warning("Account not found. \nPlease try again and make sure you've inserted one of your accounts..");
        return false;
    }

    private static boolean checkAccountToExistence(String accountTo) {
        for (Account account : AccountRepository.getAccounts().get()) {
            if (account.getAccountNumber().equals(accountTo)) {
                return true;
            }
        }
        LOGGER.warning("Account does not exist! \nPlease try again..");
        return false;
    }
}
