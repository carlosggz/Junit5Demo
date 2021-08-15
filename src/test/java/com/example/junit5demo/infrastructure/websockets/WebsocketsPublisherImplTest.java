package com.example.junit5demo.infrastructure.websockets;

import com.example.junit5demo.domain.exceptions.WebsocketPublisherException;
import com.example.junit5demo.infrastructure.config.WebsocketsConfig;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class WebsocketsPublisherImplTest {

    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    WebsocketsPublisherImpl websocketsPublisher;


    @Test
    @SneakyThrows
    void jokeIsSentToClients() {
        //given
        val givenJoke = JokeObjectMother.getRandomJoke(1);
        Mockito.doNothing().when(simpMessagingTemplate).convertAndSend(WebsocketsConfig.WEBSOCKETS_TOPIC, givenJoke);

        //when/then
        websocketsPublisher.PublishJoke(givenJoke);
    }

    @Test
    void exceptionIsReThrown() {
        //given
        val givenJoke = JokeObjectMother.getRandomJoke(1);
        Mockito.doThrow(MessagingException.class).when(simpMessagingTemplate).convertAndSend(WebsocketsConfig.WEBSOCKETS_TOPIC, givenJoke);

        //when
        assertThrows(WebsocketPublisherException.class, () -> websocketsPublisher.PublishJoke(givenJoke));
    }

}