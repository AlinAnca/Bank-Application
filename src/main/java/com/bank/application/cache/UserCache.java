package com.bank.application.cache;

import com.bank.application.model.User;
import com.bank.application.repository.UserCollection;

import java.util.List;

public class UserCache {
    private static List<User> users;

    public static List<User> getUsers(){
        if (users == null) {
            users = UserCollection.getUsers();
        }
        return users;
    }
}
