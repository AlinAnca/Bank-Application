package com.bank.application.exceptions;

public class UsernameNotFoundException extends Exception{
    public UsernameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
