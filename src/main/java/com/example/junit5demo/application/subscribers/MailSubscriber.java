package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.contracts.notifiers.MailNotifier;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.infrastructure.helpers.TemplatesHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("MailSubscriber")
public class MailSubscriber implements Subscriber {
    final static String TEMPLATE = "templates/joke-email.html";
    final MailNotifier mailNotifier;

    @Override
    @Async("taskExecutor")
    public void update(@NonNull EventInformationDto eventInformationDto) {
        log.info("Inside mail subscriber");

        if (eventInformationDto.isNew()) {
            sendMail(eventInformationDto);
        }
        else {
            log.info("Joke already existed, mail won't be send again");
        }

        log.info("Exiting mail subscriber");
    }

    private void sendMail(EventInformationDto eventInformationDto) {
        log.info("Sending mail...");

        try {
            String body = getMailBody(eventInformationDto);
            mailNotifier.sendNotification(body);
            log.info("Message was successfully sent");
        }
        catch (Exception ex) {
            log.error("Error sending mail", ex);
        }
    }

    private String getMailBody(EventInformationDto eventInformationDto) throws IOException {
        Map<String, Object> values = Map.of(
                "subject", "Joke " + eventInformationDto.getJoke().getId() + " has been recorded",
                "joke", eventInformationDto.getJoke()
        );

        return TemplatesHelper.getRenderedTemplate(TEMPLATE, values);
    }
}
