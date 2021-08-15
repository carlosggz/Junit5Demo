package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.contracts.notifiers.WebsocketsPublisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("WebsocketsSubscriber")
public class WebsocketsSubscriber implements Subscriber {
    private final WebsocketsPublisher websocketsPublisher;

    @Override
    public void update(@NonNull EventInformationDto eventInformationDto) {

        log.info("Inside websockets subscriber");

        log.info("Sending new joke to clients....");
        try {
            websocketsPublisher.PublishJoke(eventInformationDto.getJoke());
            log.info("Joke successfully sent");
        }
        catch (Exception ex) {
            log.error("Error sending websockets message", ex);
        }

        log.info("Exiting websockets subscriber");
    }
}
