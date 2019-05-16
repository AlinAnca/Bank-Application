package com.bank.application.service;

import com.bank.application.model.User;
import com.bank.application.repository.UserRepository;
import com.bank.application.validation.PaymentValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Service
public class UserService {

    private static final String LOGIN_OPTION = "1 - Login";
    private static final String ACCOUNT_OPTION = "1 - Account";
    private static final String PAY_OPTION = "2 - Make a transfer";
    private static final String LOGOUT_OPTION = "3 - Logout";
    private static final String EXIT_OPTION = "4 - Exit";

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentValidation paymentValidation;

    public void run() {
        int option = 0;
        while (option != 4) {
            if (option != 1) {
                option = getOption(LOGIN_OPTION);
            } else {
                Optional<User> user = login();
                if (user.isPresent()) {
                    do {
                        option = getOption(ACCOUNT_OPTION + "\n" + PAY_OPTION + "\n" + LOGOUT_OPTION);
                        if (option == 1) {
                            accountService.displayAccountMenu(user.get());
                        }
                        if (option == 2) {
                            paymentValidation.transferMoney(user.get());
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

    private Optional<User> login() {
        String inpUser = getNextField("username");
        String inpPass = getNextField("password");

        User user = userRepository.findByUsername(inpUser);
        if (user != null) {
            if (inpPass.equals(user.getPassword())) {
                System.out.println("\nWelcome " + user.getUsername() + "!\n");
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private String getNextField(String field) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Insert " + field + ": ");
        return keyboard.next();
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
}
