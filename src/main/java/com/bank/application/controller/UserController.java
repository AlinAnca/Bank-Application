package com.bank.application.controller;

import com.bank.application.repository.IncorrectLineException;
import com.bank.application.service.UserLogin;

import java.util.*;

public class UserController {

    public static void main(String[] args) throws IncorrectLineException {
        int option = 0;
        while(option != 3)
        {
            if(option != 1) { option = getOption("1 - Login"); }
            else {
                UserLogin obj = new UserLogin();
                boolean login = obj.run();
                if(login)
                {
                    do {
                        option = getOption("2 - Logout");
                        if(option == 2) { System.out.println("Successfully logout.\n"); }
                    }while(option != 2 && option !=3);

                } else { System.out.println("\nWrong username or password! \nPlease try again.."); }
            }
        }
    }

    private static int getOption(String message)
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please choose an option:");
        System.out.println(message);
        System.out.println("3 - Exit");
        return keyboard.nextInt();
    }
}
