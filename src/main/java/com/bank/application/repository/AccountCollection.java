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

    private final static Logger logger = Logger.getLogger(AccountCollection.class.getName());
    private final static String fileName = "file/details.txt";

    public static List<Account> getAccounts() {
        List<Account> accountList = new ArrayList<>();
        try {
            List<String> listOfLines = FileReader.readFile(fileName, 4);

            for (String line : listOfLines) {
                String[] elements = line.split("\\s");
                accountList.add(new Account(elements[0], elements[1], new BigDecimal(elements[2]), Currency.valueOf(elements[3])));
            }
        } catch (IncorrectLineException e) {
            logger.warning(e.getMessage());
        }
        return accountList;
    }
}
