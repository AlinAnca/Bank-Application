package com.bank.application.service;

import com.bank.application.model.Account;
import com.bank.application.model.User;
import com.bank.application.repository.DataCollection;
import com.bank.application.repository.UserCollection;
import com.bank.application.validation.PaymentValidation;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class UserLogin {
    private static final Logger LOGGER = Logger.getLogger(UserLogin.class.getName());

    private static final String LOGIN_OPTION = "1 - Login";
    private static final String ACCOUNT_OPTION = "1 - Account";
    private static final String PAY_OPTION = "2 - Make a transfer";
    private static final String LOGOUT_OPTION = "3 - Logout";
    private static final String EXIT_OPTION = "4 - Exit";

    public void run() {
        int option = 0;
        while (option != 4) {
            if (option != 1) {
                option = getOption(LOGIN_OPTION);
            } else {
                User user = login();
                List<Account> accountListForLoggedUser = DataCollection.getAccountsForEachUser().get(user);
                if (user != null) {
                    do {
                        option = getOption(ACCOUNT_OPTION + "\n" + PAY_OPTION + "\n" + LOGOUT_OPTION);
                        if (option == 1) {
                            AccountMenu.displayAccountMenu(user.getUsername(), accountListForLoggedUser);
                        }
                        if (option == 2) {
                            PaymentValidation.transferMoney(accountListForLoggedUser);
                            System.out.print("\n");
                        }
                        if (option == 3) {
                            LOGGER.info("Successfully logout.\n");
                        }
                    } while (option != 3 && option != 4);
                } else {
                    LOGGER.warning("Wrong username or password! \nPlease try again..\n");
                }
            }
        }
        System.out.println("Application is closed. Thank you for your time!");
    }

    private User login() {
        String inpUser = getNextField("username");
        String inpPass = getNextField("password");

        for (User user : UserCollection.getUsers()) {
            if (inpUser.equals(user.getUsername()) && inpPass.equals(user.getPassword())) {
                System.out.println("\nWelcome " + user.getUsername() + "!\n");
                return user;
            }
        }
        return null;
    }

    private int getOption(String optionMessage) {
        int option = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please choose an option: ");
        System.out.println(optionMessage);
        System.out.println(EXIT_OPTION);

        try {
            option = keyboard.nextInt();
        } catch (InputMismatchException e) {
            LOGGER.warning("Invalid option. Please try again..\n");
        }
        return option;
    }

    private String getNextField(String field) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Insert " + field + ": ");
        return keyboard.next();
    }
}
