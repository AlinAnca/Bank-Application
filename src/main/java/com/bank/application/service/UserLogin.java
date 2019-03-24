package com.bank.application.service;

import com.bank.application.model.User;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class UserLogin {
    private final String firstOption = "1 - Login";
    private final String secondOption = "2 - Logout";
    private final String exitOption = "3 - Exit";
    private Set<User> users = new HashSet<>();

    public void run() {
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

    private boolean login() {
        String inpUser = getNextField("username");
        String inpPass = getNextField("password");

        for (User u : users) {
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
