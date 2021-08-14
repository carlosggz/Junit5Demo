package com.example.junit5demo.integration;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeEvent;
import com.example.junit5demo.infrastructure.messaging.JokesChannel;
import com.example.junit5demo.infrastructure.messaging.MessagePublisherImpl;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Profile("integration")
class QueueSubscriberIntegrationTest {

    @Autowired
    MessageCollector messageCollector;

    @Autowired
    JokesChannel jokesChannel;

    @Autowired
    @Qualifier("QueueSubscriber")
    private Subscriber queueSubscriber;

    ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @SneakyThrows
    void messageIsSentToQueue(boolean isNew) {
        //given
        val joke = JokeObjectMother.getRandomJoke(1);
        val info = new EventInformationDto(joke, isNew);
        val jokeEvent = new JokeEvent(String.valueOf(joke.getId()), joke.getCategory());
        val expectedMessageContent = objectMapper.writeValueAsString(jokeEvent);

        //when
        queueSubscriber.update(info);

        //then
        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            val messages = messageCollector.forChannel(jokesChannel.output());

            assertEquals(1, messages.size());
            val message = messages.take();

            val header = message.getHeaders().get(MessagePublisherImpl.ROUTING_KEY);
            assertNull(header);

            assertEquals(expectedMessageContent, message.getPayload().toString());
        });
    }
}