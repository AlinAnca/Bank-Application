package com.bank.application.service;

import com.bank.application.cache.UserCache;
import com.bank.application.model.User;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class UserLogin {
    private final static Logger logger = Logger.getLogger(UserLogin.class.getName());
    private final String loginOption = "1 - Login";
    private final String logoutOption = "2 - Logout";
    private final String accountOption = "1 - Account";
    private final String exitOption = "3 - Exit";

    public void run() {
        int option = 0;
        while (option != 3) {
            if (option != 1) {
                option = getOption(loginOption);
            } else {
                User user = login();
                if (user != null) {
                    do {
                        option = getOption(accountOption + "\n" + logoutOption);
                        if (option == 1) {
                            AccountMenu accountMenu = new AccountMenu();
                            accountMenu.displayAccountMenu(user.getUsername());
                        }
                        if (option == 2) {
                            logger.info("Successfully logout.\n");
                        }
                    } while (option != 2 && option != 3);

                } else {
                    logger.warning("Wrong username or password! \nPlease try again..\n");
                }
            }
        }
        System.out.println("Application is closed. Thank you for your time!");
    }

    private User login() {
        String inpUser = getNextField("username");
        String inpPass = getNextField("password");

        for (User user : UserCache.getUsers()) {
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
        System.out.println(exitOption);

        try {
            option = keyboard.nextInt();
        } catch (InputMismatchException e) {
            logger.warning("Invalid option. Please try again..\n");
        }
        return option;
    }

    private String getNextField(String field) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Insert " + field + ": ");
        return keyboard.next();
    }

    public String getLoginOption() {
        return loginOption;
    }

    public String getLogoutOption() {
        return logoutOption;
    }

    public String getExitOption() {
        return exitOption;
    }
}
