package com.example.junit5demo.domain.contracts.notifiers;

import com.example.junit5demo.domain.exceptions.MailNotifierException;

public interface MailNotifier {
    void sendNotification(String body) throws MailNotifierException;
}
