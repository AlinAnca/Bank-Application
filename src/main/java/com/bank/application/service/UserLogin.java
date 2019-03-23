package com.bank.application.service;

import com.bank.application.repository.DataFile;
import com.bank.application.repository.IncorrectLineException;
import com.bank.application.model.User;

import java.util.Scanner;
import java.util.Set;

public class UserLogin {
    private static Set<User> users;

    static {
        try {
            users = new DataFile().getDataFromFile();
        } catch (IncorrectLineException e) {
            e.printStackTrace();
        }
    }

    public static boolean run()
    {
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

    private static String getNextField(String field)
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Insert " + field + ": ");
        return keyboard.next();
    }
}
