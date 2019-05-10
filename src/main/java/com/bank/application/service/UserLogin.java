package com.bank.application.service;

import com.bank.application.model.Account;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.validation.PaymentValidation;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * UserLogin is the base class which allows any valid user
 * from repository to login.
 * Once logged, user have the possibility to navigate through {@link AccountMenu},
 * to make a transfer {@link PaymentValidation}, logout or exit the application.
 *
 * @author Loredana Surugiu
 */
public class UserLogin {
    private static final Logger LOGGER = Logger.getLogger(UserLogin.class.getName());

    private static final String LOGIN_OPTION = "1 - Login";
    private static final String ACCOUNT_OPTION = "1 - Account";
    private static final String PAY_OPTION = "2 - Make a transfer";
    private static final String LOGOUT_OPTION = "3 - Logout";
    private static final String EXIT_OPTION = "4 - Exit";

    /**
     * Startup method of Login
     */
    public void run() {
        int option = 0;
        while (option != 4) {
            if (option != 1) {
                option = getOption(LOGIN_OPTION);
            } else {
                Optional<User> user = login();
                if (user.isPresent()) {
                    do {
                        List<Account> accountListForLoggedUser = AccountRepository.getAccountsFor(user.get());

                        option = getOption(ACCOUNT_OPTION + "\n" + PAY_OPTION + "\n" + LOGOUT_OPTION);
                        if (option == 1) {
                            AccountMenu.displayAccountMenu(user.get(), accountListForLoggedUser);
                        }
                        if (option == 2) {
                            PaymentValidation.transferMoney(user.get());
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

    /**
     * Gets the username and password from application.
     * Checks if input data is found in repository
     *
     * @return <code>User</code> if found;
     * nothing otherwise.
     */
    private Optional<User> login() {
        String inpUser = getNextField("username");
        String inpPass = getNextField("password");

        for (User user : UserRepository.getUsers()) {
            if (inpUser.equals(user.getUsername()) && inpPass.equals(user.getPassword())) {
                System.out.println("\nWelcome " + user.getUsername() + "!\n");
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets next input from system.
     *
     * @param field the String to display.
     * @return the next input
     */
    private String getNextField(String field) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Insert " + field + ": ");
        return keyboard.next();
    }

    /**
     * Shows to the current user possible options.
     *
     * @param optionMessage the message to display.
     * @return the next option
     */
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
}
