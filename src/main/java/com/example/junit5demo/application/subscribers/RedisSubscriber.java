package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.contracts.notifiers.RedisPublisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("RedisSubscriber")
public class RedisSubscriber implements Subscriber {
    public static final String SUBSCRIBER_KEY = "lastJoke";
    final RedisPublisher redisPublisher;

    @Override
    @Async("taskExecutor")
    public void update(@NonNull EventInformationDto eventInformationDto) {

        log.info("Inside redis subscriber");

        try {
            redisPublisher.publish(SUBSCRIBER_KEY, eventInformationDto.getJoke());
        }
        catch (Exception ex) {
            log.error("Error writing to redis", ex);
        }

        log.info("Exiting redis subscriber");
    }
}
