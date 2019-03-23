package com.bank.application.repository;

import com.bank.application.model.User;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataFile {
    private final String fileName = "file/data.txt";

    public String getFileName() {
        return fileName;
    }

    public Set<User> getDataFromFile() throws IncorrectLineException
    {
        Set<User> users = new HashSet<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scan = new Scanner(file))
        {
            //skipping headers
            scan.nextLine();

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                StringTokenizer st = new StringTokenizer(line);

                while (st.hasMoreTokens())
                {
                    if (st.countTokens() == 2)
                    {
                        users.add(new User(st.nextToken(),st.nextToken()));
                    } else {
                        throw new IncorrectLineException("Incorrect line: '" + line + "'. Number of words found: " + st.countTokens());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}