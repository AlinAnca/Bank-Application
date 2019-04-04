package com.bank.application.repository;

import com.bank.application.cache.AccountCache;
import com.bank.application.model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollection {
    public static List<Account> getAccountsForUser(String username) {

        for (Map.Entry<String, List<Account>> entry : getAccountsForEachUser().entrySet()) {
            if (entry.getKey().equals(username)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Map<String, List<Account>> getAccountsForEachUser() {
        Map<String, List<Account>> mapOfAccounts = new HashMap<>();

        for (Account account : AccountCache.getAccountsFromFile()) {
            List<Account> accounts = mapOfAccounts.get(account.getUsername());
            if (accounts == null) {
                accounts = new ArrayList<>();
                mapOfAccounts.put(account.getUsername(), accounts);
            }
            accounts.add(account);
        }
        return mapOfAccounts;
    }
}
