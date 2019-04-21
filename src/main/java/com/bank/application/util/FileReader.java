package com.bank.application.util;

import com.bank.application.exceptions.IncorrectLineException;
import com.bank.application.repository.AccountCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class FileReader {
    private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

    /**
     * Reads a file from resources and collect each valid line.
     * A valid line should have a number of elements equal to
     * a given length depending on the file.
     *
     * @param fileName        the name of the read file
     * @param lengthOfWords   the length of elements which validates the line
     * @return a collection of lines read from file
     * @throws IncorrectLineException for invalid lines
     */
    public static List<String> readFile(String fileName, int lengthOfWords) throws IncorrectLineException {
        ClassLoader classLoader = AccountCollection.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        List<String> list = new ArrayList<>();
        try (Scanner scan = new Scanner(file)) {
            scan.nextLine();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] elements = line.split("\\s");

                if (elements.length != lengthOfWords) {
                    throw new IncorrectLineException("Incorrect line: '" + line + "'. Number of words found: " + elements.length);
                } else {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            LOGGER.finest(e.getMessage());
        }
        return list;
    }
}
