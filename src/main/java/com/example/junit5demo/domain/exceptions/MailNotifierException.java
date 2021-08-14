package com.example.junit5demo.domain.exceptions;

public class MailNotifierException extends Exception {
    public MailNotifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
