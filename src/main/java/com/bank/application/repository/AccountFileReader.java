package com.bank.application.repository;

import com.bank.application.model.Account;
import com.bank.application.util.AccountType;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

public class AccountFileReader {
    private final static String fileName = "file/details.txt";
    private final static Logger logger = Logger.getLogger(AccountFileReader.class.getName());

    public static List<Account> readAccountFile(){
        ClassLoader classLoader = AccountFileReader.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        List<Account> accountList = new ArrayList<>();
        try (Scanner scan = new Scanner(file)) {
            scan.nextLine();

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] elements = line.split("\\s");

                if (elements.length == 4) {
                    accountList.add(new Account(elements[0], elements[1], new BigDecimal(elements[2]), AccountType.valueOf(elements[3])));
                }else{
                    logger.warning("Incorrect line: '" + line + "'. Number of words found:  + elements.length");
                }
            }
        } catch (IOException e) {
            logger.finest(e.getMessage());
        }
        return accountList;
    }

    public String getFileName() {
        return fileName;
    }
}
