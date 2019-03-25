package com.bank.application.service;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.User;
import com.bank.application.repository.DataFile;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserLogin {
    private final String firstOption = "1 - Login";
    private final String secondOption = "2 - Logout";
    private final String exitOption = "3 - Exit";

    public void run() throws IncorrectLineException {
        int option = 0;
        while (option != 3) {
            if (option != 1) {
                option = getOption(firstOption);
            } else {
                if (login()) {
                    do {
                        option = getOption(secondOption);
                        if (option == 2) {
                            System.out.println("Successfully logout.\n");
                        }
                    } while (option != 2 && option != 3);

                } else {
                    System.out.println("\nWrong username or password! \nPlease try again..");
                }
            }
        }
    }

    private boolean login() throws IncorrectLineException {
        String inpUser = getNextField("username");
        String inpPass = getNextField("password");

        for (User u : DataFile.getUsers()) {
            if (inpUser.equals(u.getUsername()) && inpPass.equals(u.getPassword())) {
                System.out.println("\nWelcome " + u.getUsername() + "!");
                return true;
            }
        }
        return false;
    }

    private int getOption(String optionMessage) {
        int option = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please choose an option:");
        System.out.println(optionMessage);
        System.out.println(exitOption);

        try {
            option = keyboard.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid option. Please try again..");
        }

        return option;
    }

    private String getNextField(String field) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Insert " + field + ": ");
        return keyboard.next();
    }

    public String getFirstOption() {
        return firstOption;
    }

    public String getSecondOption() {
        return secondOption;
    }

    public String getExitOption() {
        return exitOption;
    }
}
