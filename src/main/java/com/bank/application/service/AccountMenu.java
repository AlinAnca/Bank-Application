package com.bank.application.service;

import com.bank.application.cache.AccountCache;
import com.bank.application.model.Account;
import com.bank.application.util.AccountFileWriter;
import com.bank.application.util.AccountValidation;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class AccountMenu {
    private final static String fileName = "file/details.txt";
    private final static Logger logger = Logger.getLogger(UserLogin.class.getName());

    private static final String createAccountOption = "1 - Create Account";
    private static final String displayAccountsOption = "2 - Display Accounts";
    private static final String backOption = "3 - Back";

    public static void displayAccountMenu(String username) {
        int option;
        do {
            option = getOptions();
            if (option == 1) {
                createAccount(username);
                logger.info("Successfully created account!\n");
            }
            if (option == 2) {
                inspectAccount(username);
            }
        } while (option != 3);
    }

    private static int getOptions() {
        int option = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Your options: ");
        System.out.println(createAccountOption);
        System.out.println(displayAccountsOption);
        System.out.println(backOption);

        try {
            option = keyboard.nextInt();
        } catch (InputMismatchException e) {
            logger.warning("Invalid option! Please try again..\n");
        }
        return option;
    }

    public static void createAccount(String username) {
        Account account = AccountValidation.getAccount(username);
        AccountCache.addAccount(account);
        AccountFileWriter.writeAccountToFile(account, fileName);
    }

    public static void inspectAccount(String username) {
        for (Account account : AccountCache.getAccountsFromFile()) {
            if (account.getUsername().equals(username)) {
                logger.info("\nUsername: " + account.getUsername() + "\nAccount: " + account.getAccountNumber() +
                        "\nBalance: " + account.getBalance() + "\nCurrency: " + account.getCurrency() + "\n");
            }
        }
    }
}
