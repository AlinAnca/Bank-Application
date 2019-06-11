package com.bank.application.exceptions;

public class UserAlreadyLoggedException extends Exception {
    public UserAlreadyLoggedException(String message) {
        super(message);
    }
}
