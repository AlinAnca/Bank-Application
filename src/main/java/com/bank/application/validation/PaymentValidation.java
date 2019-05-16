package com.bank.application.validation;

import com.bank.application.model.Account;
import com.bank.application.model.Notification;
import com.bank.application.model.Transaction;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.repository.NotificationRepository;
import com.bank.application.repository.TransactionRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.util.Currency;
import com.bank.application.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Service
public class PaymentValidation {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public void transferMoney(User user) {
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
            createNotification(user, details);
        }
    }

    private void createNotification(User user, String details) {
        notificationRepository.save(Notification.builder()
                .withUser(userRepository.findByUsername(user.getUsername()))
                .withDetails(details)
                .build()
        );
    }

    private void depositAmount(String accountNumberFrom, double amount, String accountNumberTo, String details) {
        Optional<Account> fromAccount = accountRepository.findAccountByAccountNumber(accountNumberFrom);
        Optional<Account> toAccount = accountRepository.findAccountByAccountNumber(accountNumberTo);

        if (fromAccount.isPresent()) {
            BigDecimal newBalance = fromAccount.get().getBalance().subtract(new BigDecimal(amount));
            accountRepository.updateAccountBalance(newBalance, fromAccount.get().getId());
            createTransaction(accountNumberTo, amount, fromAccount.get(), details, Type.INCOMING);
        }
        if (toAccount.isPresent()) {
            BigDecimal newBalance = toAccount.get().getBalance().add(new BigDecimal(amount));
            accountRepository.updateAccountBalance(newBalance, toAccount.get().getId());
            createTransaction(accountNumberFrom, amount, toAccount.get(), details, Type.OUTGOING);
        }
        LOGGER.info("Transfer has been processed successfully.");
    }

    private void createTransaction(String accountNumber, double amount, Account account, String details, Type type) {
        if (account != null) {
            transactionRepository.save(
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

    private double getAmount() {
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

    private boolean checkAmount(double amount, String accountFrom) {
        double amountAv = getAvailableAmount(accountFrom).doubleValue();
        if (amount <= 0 || amount > amountAv) {
            LOGGER.warning("Invalid amount.");
            return false;
        }
        return true;
    }

    private BigDecimal getAvailableAmount(String accountFrom) {
        BigDecimal availableAmount = null;
        Optional<Account> account = accountRepository.findAccountByAccountNumber(accountFrom);
        if (account.isPresent()) {
            availableAmount = account.get().getBalance();
        }
        return availableAmount;
    }

    private boolean checkAccountTo(String accountFrom, String accountTo) {
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

    private boolean checkAccountFrom(String accountFrom) {
        Optional<Account> account = accountRepository.findAccountByAccountNumber(accountFrom);

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

    private Optional<Currency> getAccountType(String account) {
        Optional<Account> accountOptional = accountRepository.findAccountByAccountNumber(account);
        if (accountOptional.isPresent()) {
            return Optional.of(accountOptional.get().getCurrency());
        }
        return Optional.empty();
    }

    private boolean checkAccountUniquenessForEachCurrency() {
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

    private Map<Currency, Long> getNumberOfAccountsByCurrency() {
        return accountRepository.findAll().stream()
                .collect(Collectors.groupingBy(Account::getCurrency, Collectors.counting()));
    }

    private boolean checkAccountUniqueness() {
        if (accountRepository.findAll().size() == 1) {
            LOGGER.warning("You are not able to make a transfer. Only one account founded!");
            return true;
        }
        return false;
    }

    private boolean checkAccountExistence(String account) {
        if (accountRepository.findAccountByAccountNumber(account).isPresent()) {
            return true;
        }
        LOGGER.warning("Account not found. \nPlease try again..");
        return false;
    }
}
