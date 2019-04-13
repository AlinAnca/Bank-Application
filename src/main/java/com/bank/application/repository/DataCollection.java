package com.bank.application.repository;

import com.bank.application.model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollection {

    public static Map<String, List<Account>> getAccountsForEachUser() {
        Map<String, List<Account>> mapOfAccounts = new HashMap<>();
        for (Account account : AccountCollection.getAccounts()) {
            List<Account> accounts = mapOfAccounts.computeIfAbsent(account.getUsername(), k -> new ArrayList<>());
            accounts.add(account);
        }
        return mapOfAccounts;
    }
}
