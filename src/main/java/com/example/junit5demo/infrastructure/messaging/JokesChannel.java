package com.example.junit5demo.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface JokesChannel {
    String OUTPUT = "JokesChannel";

    @Output(JokesChannel.OUTPUT)
    MessageChannel output();
}
