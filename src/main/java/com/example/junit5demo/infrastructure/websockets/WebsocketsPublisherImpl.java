package com.example.junit5demo.infrastructure.websockets;

import com.example.junit5demo.domain.contracts.notifiers.WebsocketsPublisher;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.WebsocketPublisherException;
import com.example.junit5demo.infrastructure.config.WebsocketsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebsocketsPublisherImpl implements WebsocketsPublisher {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void PublishJoke(JokeDto value) throws WebsocketPublisherException {
        try {
            simpMessagingTemplate.convertAndSend(WebsocketsConfig.WEBSOCKETS_TOPIC, value);
        }
        catch (Exception ex) {
            throw new WebsocketPublisherException("Error sending websockets message", ex);
        }
    }
}
