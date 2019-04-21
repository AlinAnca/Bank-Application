package com.bank.application.repository;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.model.User;
import com.bank.application.util.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * UserCollection is the class which converts the data from
 * file {@link file/users.txt} into a collection of users.
 */
public class UserCollection {

    private static final Logger LOGGER = Logger.getLogger(UserCollection.class.getName());
    private static final String FILE_NAME = "file/users.txt";

    /**
     * Gets data from file and adds it into a collection of users.
     * Catches invalid data and sends warnings.
     * @return the collection of available users
     */
    public static List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        try {
            List<String> listOfLines = FileReader.readFile(FILE_NAME, 2);

            for (String line : listOfLines) {
                String[] elements = line.split("\\s");
                userList.add(new User(elements[0], elements[1]));
            }
        } catch (IncorrectLineException e) {
            LOGGER.warning(e.getMessage());
        }
        return userList;
    }
}