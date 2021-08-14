package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.notifiers.RedisPublisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.RedisNotifierException;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RedisSubscriberTest {

    @Mock
    RedisPublisher redisPublisher;

    @InjectMocks
    RedisSubscriber redisSubscriber;

    @Captor
    ArgumentCaptor<JokeDto> captor;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @SneakyThrows
    void messageIsSentToNotifier(boolean isNew) {
        //given
        val givenJoke = JokeObjectMother.getRandomJoke(1);
        val info = new EventInformationDto(givenJoke, isNew);

        //when
        redisSubscriber.update(info);

        //then
        Mockito.verify(redisPublisher).publish(Mockito.any(String.class), captor.capture());
        val actualJoke = captor.getValue();
        assertNotNull(actualJoke);
        assertEquals(givenJoke, actualJoke);
    }

    @Test
    @SneakyThrows
    void whenPublisherThrowsExceptionItIsCaught() {
        //given
        val givenJoke = JokeObjectMother.getRandomJoke(1);
        val info = new EventInformationDto(givenJoke, true);
        Mockito.doThrow(RedisNotifierException.class).when(redisPublisher).publish(Mockito.any(), Mockito.any());

        //when/then
        redisSubscriber.update(info);
    }
}