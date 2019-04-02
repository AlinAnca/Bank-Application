package com.bank.application.repository;

import com.bank.application.model.User;
import com.bank.application.util.FileReader;

import java.util.*;

public class UserCollection {
    private final static String fileName = "file/users.txt";

    public static List<User> getUsers(){
        List<String> listOfLines = FileReader.readFile(fileName,2);

        List<User> userList = new ArrayList<>();
        for(String line: listOfLines){
            String[] elements = line.split("\\s");
            userList.add(new User(elements[0], elements[1]));
        }
        return userList;
    }

    public String getFileName() {
        return fileName;
    }
}