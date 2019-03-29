package com.bank.application.repository;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.User;
import com.bank.application.service.UserLogin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class UserFileReader {
    private final static String fileName = "file/users.txt";
    private final static Logger logger = Logger.getLogger(UserLogin.class.getName());

    public static List<User> readFile() throws IncorrectLineException {
        ClassLoader classLoader = UserFileReader.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        List<User> users = new ArrayList<>();
        try (Scanner scan = new Scanner(file)) {
            scan.nextLine();

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] elements = line.split("\\s");

                if (elements.length == 2) {
                    users.add(new User(elements[0], elements[1]));
                } else {
                    throw new IncorrectLineException("Incorrect line: '" + line + "'. Number of words found: " + elements.length);
                }
            }
        } catch (IOException e) {
            logger.finest(e.getMessage());
        }
        return users;
    }

    public String getFileName() {
        return fileName;
    }
}