package com.bank.application.service;

import com.bank.application.model.Account;
import com.bank.application.util.AccountFileWriter;
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

    private static final String FILE_NAME = "file/details.txt";

    private static final String CREATE_OPTION = "1 - Create Account";
    private static final String DISP_OPTION = "2 - Display Accounts";
    private static final String BACK_OPTION = "3 - Back";

    /**
     * Displays account options for creating and inspecting available accounts.
     * @param username     the username of the current user
     * @param accountList  the account list of the current user
     */
    static void displayAccountMenu(String username, List<Account> accountList) {
        int option;
        do {
            option = getOptions();
            if (option == 1) {
                createAccount(username, accountList);
                LOGGER.info("Successfully created account!\n");
            }
            if (option == 2) {
                inspectAccount(accountList);
            }
        } while (option != 3);
    }

    /**
     * Shows to the current user possible options.
     * @return option  the entered option
     */
    private static int getOptions() {
        int option = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Your options: ");
        System.out.println(CREATE_OPTION);
        System.out.println(DISP_OPTION);
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
     * Writes accounts to {@link file/details.txt}
     * @param username     the username that identifies the current user
     * @param accountList  the accounts of current user
     */
    private static void createAccount(String username, List<Account> accountList) {
        Account account = AccountValidation.getAccount(username);
        accountList.add(account);
        AccountFileWriter.writeAccountToFile(account, FILE_NAME);
    }

    /**
     * Inspects current user accounts and print the details
     * @param accountList  the available accounts of user
     */
    private static void inspectAccount(List<Account> accountList) {
        for (Account account : accountList) {
            LOGGER.info("\nUsername: " + account.getUsername() + "\nAccount: " + account.getAccountNumber() +
                    "\nBalance: " + account.getBalance() + "\nCurrency: " + account.getCurrency() + "\n");
        }
    }
}
