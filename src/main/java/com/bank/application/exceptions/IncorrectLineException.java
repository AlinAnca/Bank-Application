package com.bank.application.exceptions;

/**
 * Custom exception which it's handle whenever a line from a file
 * does not meet the requirements in a specific context.
 */
public class IncorrectLineException extends Exception {
    public IncorrectLineException(String message) {
        super(message);
    }
}
