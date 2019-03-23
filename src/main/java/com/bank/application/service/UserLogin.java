package com.bank.application.service;

import com.bank.application.repository.DataFile;
import com.bank.application.repository.IncorrectLineException;
import com.bank.application.model.User;

import java.util.Scanner;
import java.util.Set;

public class UserLogin
{
    public boolean run() throws IncorrectLineException {
        Set<User> users = new DataFile().getDataFromFile();

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

    private String getNextField(String field)
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Insert " + field + ": ");
        return keyboard.next();
    }
}
