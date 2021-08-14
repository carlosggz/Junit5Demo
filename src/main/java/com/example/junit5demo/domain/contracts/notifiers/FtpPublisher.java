package com.example.junit5demo.domain.contracts.notifiers;

import com.example.junit5demo.domain.exceptions.FtpNotifierException;

import java.io.InputStream;

public interface FtpPublisher {
    void publishFile(String fileName, InputStream fileContent) throws FtpNotifierException;
}
