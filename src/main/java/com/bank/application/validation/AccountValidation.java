package com.bank.application.validation;

import com.bank.application.model.Account;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.util.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Service
public class AccountValidation {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    public Account getAccount(User user) {
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

        return Account.builder()
                .withUser(userRepository.findByUsername(user.getUsername()))
                .withAccountNumber(accountNumber)
                .withBalance(new BigDecimal(amount))
                .withUpdatedTime(LocalDateTime.now())
                .withCurrency(Currency.valueOf(accountType))
                .build();
    }

    private double getAmount() {
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

    private boolean checkAmount(double amount) {
        if (amount < 0) {
            LOGGER.warning("Invalid amount.");
            return false;
        }
        return true;
    }

    private boolean checkAccountType(String accountType) {
        String type = accountType.toUpperCase().trim();
        if (type.equals("RON") || type.equals("EUR")) {
            return true;
        }
        LOGGER.warning("Invalid type.");
        return false;
    }

    private boolean checkAccountNumber(String accountNumber) {
        if (!accountNumber.toUpperCase().startsWith("RO")) {
            LOGGER.warning("Invalid account number: " + accountNumber + ". It should start with 'RO'");
            return false;
        } else if (accountNumber.length() != 24) {
            LOGGER.warning("Invalid account number: " + accountNumber + ". Account number length should be 24.");
            return false;
        }
        return checkAccountNumberUniqueness(accountNumber);
    }

    private boolean checkAccountNumberUniqueness(String accountNumber) {
        Optional<Account> account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()) {
            LOGGER.warning("Account number already exists!\nPlease try again.. ");
            return false;
        }
        return true;
    }
}
