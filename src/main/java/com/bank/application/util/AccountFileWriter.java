package com.bank.application.util;

import com.bank.application.model.Account;
import com.bank.application.service.AccountMenu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class AccountFileWriter {
    private static final Logger LOGGER = Logger.getLogger(AccountFileWriter.class.getName());

    /**
     * Writes an account to a file from resources.
     * @param account  the account number
     * @param fileName the name of the file in which it's kept the account data
     */
    public static void writeAccountToFile(Account account, String fileName) {
        ClassLoader classLoader = AccountMenu.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.newLine();
            writer.write(account.getAccountNumber() + " ");
            writer.write(account.getUsername() + " ");
            writer.write(account.getBalance() + " ");
            writer.write(account.getCurrency() + " ");
        } catch (IOException e) {
            LOGGER.finest(e.getMessage());
        }
    }
}
