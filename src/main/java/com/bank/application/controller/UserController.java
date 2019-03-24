package com.bank.application.controller;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.repository.DataFile;
import com.bank.application.service.UserLogin;

public class UserController {

    public static void main(String[] args) throws IncorrectLineException {
        UserLogin login = new UserLogin();
        login.setUsers(new DataFile().readFile());
        login.run();
    }
}
