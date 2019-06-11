package com.bank.application.exceptions;

public class DuplicateAccountNumberException extends Exception {
    public DuplicateAccountNumberException(String message) {
        super(message);
    }
}
