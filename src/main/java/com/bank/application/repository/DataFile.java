package com.bank.application.repository;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.User;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DataFile {
    private static Set<User> users = new HashSet<>();
    private final String fileName = "file/data.txt";

    public String getFileName() {
        return fileName;
    }

    public Set<User> readFile() throws IncorrectLineException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scan = new Scanner(file)) {
            //skipping headers
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
            e.printStackTrace();
        }
        return users;
    }
}