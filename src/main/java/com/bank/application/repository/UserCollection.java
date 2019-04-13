package com.bank.application.repository;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.User;
import com.bank.application.util.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserCollection {

    private final static Logger logger = Logger.getLogger(UserCollection.class.getName());
    private final static String fileName = "file/users.txt";

    public static List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        try {
            List<String> listOfLines = FileReader.readFile(fileName, 2);

            for (String line : listOfLines) {
                String[] elements = line.split("\\s");
                userList.add(new User(elements[0], elements[1]));
            }
        } catch (IncorrectLineException e) {
            logger.warning(e.getMessage());
        }
        return userList;
    }
}