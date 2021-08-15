package com.example.junit5demo.domain.contracts.notifiers;

import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.WebsocketPublisherException;

public interface WebsocketsPublisher {
    void PublishJoke(JokeDto value) throws WebsocketPublisherException;
}
