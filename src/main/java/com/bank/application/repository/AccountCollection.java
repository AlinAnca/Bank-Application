package com.bank.application.repository;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.Account;
import com.bank.application.util.Currency;
import com.bank.application.util.FileReader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AccountCollection {

    private static final Logger LOGGER = Logger.getLogger(AccountCollection.class.getName());
    private static final String FILE_NAME = "file/details.txt";

    public static List<Account> getAccounts() {
        List<Account> accountList = new ArrayList<>();
        try {
            List<String> listOfLines = FileReader.readFile(FILE_NAME, 4);

            for (String line : listOfLines) {
                String[] elements = line.split("\\s");
                accountList.add(new Account(elements[0], elements[1], new BigDecimal(elements[2]), Currency.valueOf(elements[3])));
            }
        } catch (IncorrectLineException e) {
            LOGGER.warning(e.getMessage());
        }
        return accountList;
    }
}
