package com.bank.application.cache;

import com.bank.application.model.Account;
import com.bank.application.repository.AccountFileReader;

import java.util.List;

public class AccountCache {
    private static List<Account> accountList;

    public static List<Account> getAccountsFromFile() {
        if (accountList == null) {
            accountList = AccountFileReader.readAccountFile();
        }
        return accountList;
    }

    public static void addAccount(Account account){
        accountList.add(account);
    }
}
