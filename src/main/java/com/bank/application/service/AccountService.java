package com.bank.application.service;

import com.bank.application.model.Account;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.validation.AccountValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Service
public class AccountService {

    private static final String CREATE_OPTION = "1 - Create Account";
    private static final String DISPLAY_OPTION = "2 - Display Accounts";
    private static final String BACK_OPTION = "3 - Back";

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountValidation accountValidation;

    private static int getOptions() {
        int option = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Your options: ");
        System.out.println(CREATE_OPTION);
        System.out.println(DISPLAY_OPTION);
        System.out.println(BACK_OPTION);

        try {
            option = keyboard.nextInt();
        } catch (InputMismatchException e) {
            LOGGER.warning("Invalid option! Please try again..\n");
        }
        return option;
    }

    public void displayAccountMenu(User user) {
        int option;
        do {
            option = getOptions();
            if (option == 1) {
                createAccount(user);
                LOGGER.info("Successfully created account!\n");
            }
            if (option == 2) {
                inspectAccount(user);
            }
        } while (option != 3);
    }

    private void createAccount(User user) {
        Account account = accountValidation.getAccount(user);
        accountRepository.save(account);
    }

    private void inspectAccount(User user) {
        List<Account> accountListForLoggedUser = accountRepository.findAccountsBy(user);
        if (accountListForLoggedUser.size() > 0) {
            for (Account account : accountListForLoggedUser) {
                LOGGER.info("\nUsername: " + account.getUser().getUsername() + "\nAccount: " + account.getAccountNumber() +
                        "\nBalance: " + account.getBalance() + "\nCurrency: " + account.getCurrency() + "\n");
            }
        } else {
            LOGGER.info("No available accounts.\n");
        }
    }
}
