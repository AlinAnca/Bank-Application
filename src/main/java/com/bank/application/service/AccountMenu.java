package com.bank.application.service;

import com.bank.application.model.Account;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.validation.AccountValidation;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * AccountMenu is the class where the logged user has the options
 * to create an account or view his available accounts.
 */
public class AccountMenu {
    private static final Logger LOGGER = Logger.getLogger(UserLogin.class.getName());

    private static final String CREATE_OPTION = "1 - Create Account";
    private static final String DISPLAY_OPTION = "2 - Display Accounts";
    private static final String BACK_OPTION = "3 - Back";

    /**
     * Displays account options for creating and inspecting available accounts.
     *
     * @param user        the current user
     * @param accountList the account list of the current user
     */
    static void displayAccountMenu(User user, List<Account> accountList) {
        int option;
        do {
            option = getOptions();
            if (option == 1) {
                createAccount(user, accountList);
                LOGGER.info("Successfully created account!\n");
            }
            if (option == 2) {
                inspectAccount(accountList);
            }
        } while (option != 3);
    }

    /**
     * Shows to the current user possible options.
     *
     * @return option  the entered option
     */
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

    /**
     * Creates an account for user and update his account list.
     * Adds account to account repository.
     *
     * @param user the current user
     */
    private static void createAccount(User user, List<Account> accountList) {
        Account account = AccountValidation.getAccount(user);
        accountList.add(account);
        AccountRepository.addAccount(account);
    }

    /**
     * Inspects current user accounts and print the details.
     *
     * @param accountList the available accounts of user
     */
    private static void inspectAccount(List<Account> accountList) {
        if (accountList.size() > 0) {
            for (Account account : accountList) {
                LOGGER.info("\nUsername: " + account.getUser().getUsername() + "\nAccount: " + account.getAccountNumber() +
                        "\nBalance: " + account.getBalance() + "\nCurrency: " + account.getCurrency() + "\n");
            }
        } else {
            LOGGER.info("No available accounts.\n");
        }
    }
}
