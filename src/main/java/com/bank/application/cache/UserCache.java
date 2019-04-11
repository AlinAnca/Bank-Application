package com.bank.application.cache;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.User;
import com.bank.application.repository.UserCollection;

import java.util.List;
import java.util.logging.Logger;

public class UserCache {
    private final static Logger logger = Logger.getLogger(AccountCache.class.getName());
    private static List<User> users;

    public static List<User> getUsers() {
        try {
            if (users == null) {
                users = UserCollection.getUsers();
            }
        } catch (IncorrectLineException e) {
            logger.warning(e.getMessage());
        }
        return users;
    }
}
