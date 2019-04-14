package com.bank.application.repository;

import com.bank.application.model.Account;
import com.bank.application.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollection {

    public static Map<User, List<Account>> getAccountsForEachUser() {
        Map<User, List<Account>> mapOfAccounts = new HashMap<>();

        for (Account account : AccountCollection.getAccounts()) {
            for(User user: UserCollection.getUsers()) {
                if (account.getUsername().equals(user.getUsername())) {
                    List<Account> accounts = mapOfAccounts.computeIfAbsent(user, k -> new ArrayList<>());
                    accounts.add(account);
                }
            }
        }
        return mapOfAccounts;
    }
}
