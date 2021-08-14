package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.contracts.notifiers.MessagePublisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("QueueSubscriber")
public class QueueSubscriber implements Subscriber {
    final MessagePublisher<JokeEvent> messagePublisher;

    @Override
    @Async("taskExecutor")
    public void update(@NonNull EventInformationDto eventInformationDto) {
        log.info("Inside queue subscriber");

        try {
            log.info("Publishing message...");

            JokeEvent event = new  JokeEvent(
                    String.valueOf(eventInformationDto.getJoke().getId()),
                    eventInformationDto.getJoke().getCategory()
            );

            messagePublisher.publishEvent(event);
        }
        catch (Exception ex) {
            log.error("Unexpected error publishing message", ex);
        }

        log.info("Exiting queue subscriber");
    }
}
