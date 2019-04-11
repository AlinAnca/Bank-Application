package com.bank.application.cache;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.Account;
import com.bank.application.repository.AccountCollection;

import java.util.List;
import java.util.logging.Logger;

public class AccountCache {
    private final static Logger logger = Logger.getLogger(AccountCache.class.getName());
    private static List<Account> accountList;

    public static List<Account> getAccountsFromFile() {
        try {
            if (accountList == null) {
                accountList = AccountCollection.getAccounts();
            }
        } catch (IncorrectLineException e) {
            logger.warning(e.getMessage());
        }
        return accountList;
    }

    public static void addAccount(Account account) {
        accountList.add(account);
    }
}
