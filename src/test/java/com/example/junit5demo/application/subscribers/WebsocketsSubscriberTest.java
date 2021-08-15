package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.notifiers.WebsocketsPublisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.exceptions.WebsocketPublisherException;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebsocketsSubscriberTest {
    @Mock
    WebsocketsPublisher websocketsPublisher;

    @InjectMocks
    WebsocketsSubscriber websocketsSubscriber;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @SneakyThrows
    public void messageIsSentToTheClients(boolean isNew) {
        //given
        val givenJoke = JokeObjectMother.getRandomJoke(1);
        val info = new EventInformationDto(givenJoke, isNew);

        //when
        websocketsSubscriber.update(info);

        //then
        Mockito.verify(websocketsPublisher).PublishJoke(givenJoke);
    }

    @Test
    @SneakyThrows
    public void whenPublisherThrowsExceptionItIsCaught() {
        //given
        val givenJoke = JokeObjectMother.getRandomJoke(1);
        val info = new EventInformationDto(givenJoke, true);
        Mockito.doThrow(WebsocketPublisherException.class).when(websocketsPublisher).PublishJoke(givenJoke);

        //when/then
        websocketsSubscriber.update(info);
    }
}