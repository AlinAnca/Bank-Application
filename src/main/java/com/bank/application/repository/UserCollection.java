package com.bank.application.repository;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.User;
import com.bank.application.util.FileReader;

import java.util.ArrayList;
import java.util.List;

public class UserCollection {
    private final static String fileName = "file/users.txt";

    public static List<User> getUsers() throws IncorrectLineException {
        List<String> listOfLines = FileReader.readFile(fileName, 2);

        List<User> userList = new ArrayList<>();
        for (String line : listOfLines) {
            String[] elements = line.split("\\s");
            userList.add(new User(elements[0], elements[1]));
        }
        return userList;
    }

    public String getFileName() {
        return fileName;
    }
}