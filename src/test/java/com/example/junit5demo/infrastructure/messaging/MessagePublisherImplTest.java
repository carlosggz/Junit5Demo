package com.example.junit5demo.infrastructure.messaging;

import com.example.junit5demo.domain.dtos.JokeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessagePublisherImplTest {
    @Autowired
    MessageCollector messageCollector;

    @Autowired
    JokesChannel jokesChannel;

    @Autowired
    MessagePublisherImpl<JokeEvent> queueNotifier;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void sendMessagePutsAMessageInQueueWithoutRoutingKey() {
        //given
        val jokeEvent = new JokeEvent("123", "category");
        val expectedMessageContent = objectMapper.writeValueAsString(jokeEvent);

        //when
        queueNotifier.publishEvent(jokeEvent);

        //then
        val messages = messageCollector.forChannel(jokesChannel.output());

        assertEquals(1, messages.size());
        val message = messages.take();

        val header = message.getHeaders().get(MessagePublisherImpl.ROUTING_KEY);
        assertNull(header);

        assertEquals(expectedMessageContent, message.getPayload().toString());
    }

    @Test
    @SneakyThrows
    void sendMessagePutsAMessageInQueueWithRoutingKey() {
        //given
        val jokeEvent = new JokeEvent("123", "category");
        val expectedMessageContent = objectMapper.writeValueAsString(jokeEvent);
        val routingKey = "my.key";

        //when
        queueNotifier.publishEvent(jokeEvent, routingKey);

        //then
        val messages = messageCollector.forChannel(jokesChannel.output());

        assertEquals(1, messages.size());
        val message = messages.take();

        val header = message.getHeaders().get(MessagePublisherImpl.ROUTING_KEY);
        assertNotNull(header);
        assertEquals(routingKey, header);

        assertEquals(expectedMessageContent, message.getPayload().toString());
    }
}