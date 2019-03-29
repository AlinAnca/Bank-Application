package com.bank.application.cache;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.User;
import com.bank.application.repository.UserFileReader;

import java.util.List;

public class UserCache {
    private static List<User> users;

    public static List<User> getUsers() throws IncorrectLineException {
        if (users == null) {
            users = UserFileReader.readFile();
        }
        return users;
    }
}
