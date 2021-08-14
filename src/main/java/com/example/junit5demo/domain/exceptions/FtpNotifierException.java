package com.example.junit5demo.domain.exceptions;

public class FtpNotifierException extends Exception {
    public FtpNotifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
