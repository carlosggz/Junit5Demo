package com.example.junit5demo.domain.exceptions;

public class RedisNotifierException extends Exception {
    public RedisNotifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
