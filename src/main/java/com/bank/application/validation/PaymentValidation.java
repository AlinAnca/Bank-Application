package com.bank.application.validation;

import com.bank.application.model.Account;
import com.bank.application.model.Notification;
import com.bank.application.model.Transaction;
import com.bank.application.repository.AccountRepository;
import com.bank.application.repository.NotificationRepository;
import com.bank.application.repository.TransactionRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.util.Currency;
import com.bank.application.util.Type;
import com.bank.application.view.UserView;

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
    public static void transferMoney(UserView userView) {
        Scanner keyboard = new Scanner(System.in);
        String accountFrom;
        double amount;
        String accountTo;
        String details;

        if (!checkAccountUniquenessForEachCurrency()) {
            do {
                System.out.print("Insert Account from which you want to make a transfer: ");
                accountFrom = keyboard.next();
            } while (!checkAccountExistence(accountFrom) || !checkAccountFrom(accountFrom));

            do {
                amount = getAmount();
            } while (!checkAmount(amount, accountFrom));

            do {
                System.out.print("Insert Account to which you want to send the money: ");
                accountTo = keyboard.next();
            } while (!checkAccountExistence(accountTo) || !checkAccountTo(accountFrom, accountTo));

            System.out.print("Description: ");
            details = new Scanner(System.in).nextLine();

            depositAmount(accountFrom, amount, accountTo, details);
            createNotification(userView, details);
        }
    }

    /**
     * Creates a notification into Database
     *
     * @param userView the current user
     * @param details  the description
     */
    private static void createNotification(UserView userView, String details) {
        NotificationRepository.addNotification(Notification.builder()
                .withUser(UserRepository.getUserByUsername(userView.getUsername()))
                .withDetails(details)
                .build()
        );
    }

    /**
     * Deposits the amount from an account to another from the current user's account list.
     *
     * @param accountNumberFrom the account from which is made the transfer
     * @param amount            the amount to transfer
     * @param accountNumberTo   the account to which is made the transfer
     * @param details           the description of transaction
     */
    private static void depositAmount(String accountNumberFrom, double amount, String accountNumberTo, String details) {
        Optional<Account> fromAccount = AccountRepository.getAccountByAccountNumber(accountNumberFrom);
        Optional<Account> toAccount = AccountRepository.getAccountByAccountNumber(accountNumberTo);

        if (fromAccount.isPresent()) {
            BigDecimal newBalance = fromAccount.get().getBalance().subtract(new BigDecimal(amount));
            AccountRepository.updateAccountBalance(fromAccount.get().getId(), newBalance);
            createTransaction(accountNumberTo, amount, fromAccount.get(), details, Type.INCOMING);
        }
        if (toAccount.isPresent()) {
            BigDecimal newBalance = toAccount.get().getBalance().add(new BigDecimal(amount));
            AccountRepository.updateAccountBalance(toAccount.get().getId(), newBalance);
            createTransaction(accountNumberFrom, amount, toAccount.get(), details, Type.OUTGOING);
        }
        LOGGER.info("Transfer has been processed successfully.");
    }

    /**
     * Inserts an incoming or outgoing transaction into Database
     *
     * @param accountNumber the account number
     * @param amount        the amount to transfer
     * @param account       the account to/from which is made the transfer
     * @param details       the description of transaction
     * @param type          the type of transaction
     */
    private static void createTransaction(String accountNumber, double amount, Account account, String details, Type type) {
        if (account != null) {
            TransactionRepository.addTransaction(
                    Transaction.builder()
                            .withAccountNumber(accountNumber)
                            .withAmount(new BigDecimal(amount))
                            .withAccount(account)
                            .withDetails(details)
                            .withType(type)
                            .build()
            );
        }
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
     * @param amount      the amount to check;
     * @param accountFrom the account from which is made the transfer
     * @return <code>true</code> if the amount is valid;
     * <code>false</code> otherwise
     */
    private static boolean checkAmount(double amount, String accountFrom) {
        double amountAv = getAvailableAmount(accountFrom).doubleValue();
        if (amount <= 0 || amount > amountAv) {
            LOGGER.warning("Invalid amount.");
            return false;
        }
        return true;
    }

    /**
     * Gets available amount of the account from which the transfer is made.
     *
     * @param accountFrom the account number from which is made the transfer
     * @return the available amount from account
     */
    private static BigDecimal getAvailableAmount(String accountFrom) {
        BigDecimal availableAmount = null;
        Optional<Account> account = AccountRepository.getAccountByAccountNumber(accountFrom);
        if (account.isPresent()) {
            availableAmount = account.get().getBalance();
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
     * @param accountFrom the account form which is made the transfer
     * @return <code>true</code> if the account is valid;
     * <code>false</code> otherwise
     */
    private static boolean checkAccountFrom(String accountFrom) {
        Optional<Account> account = AccountRepository.getAccountByAccountNumber(accountFrom);

        if (account.isPresent() && account.get().getBalance().equals(new BigDecimal(0))) {
            LOGGER.warning("You don't have enough money in your account. \nPlease try again..");
            return false;
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
        Optional<Account> accountOptional = AccountRepository.getAccountByAccountNumber(account);
        if (accountOptional.isPresent()) {
            return Optional.of(accountOptional.get().getCurrency());
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
     * @param account the account checked
     * @return <code>true</code> if the account exists;
     * <code>false</code> otherwise
     */
    private static boolean checkAccountExistence(String account) {
        if (AccountRepository.getAccountByAccountNumber(account).isPresent()) {
            return true;
        }
        LOGGER.warning("Account not found. \nPlease try again..");
        return false;
    }
}
