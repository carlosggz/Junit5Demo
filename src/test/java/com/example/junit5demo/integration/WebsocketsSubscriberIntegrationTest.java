package com.example.junit5demo.integration;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.infrastructure.websockets.WebsocketsPublisherImpl;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("integration")
class WebsocketsSubscriberIntegrationTest {

    @Autowired
    @Qualifier("WebsocketsSubscriber")
    private Subscriber websocketsSubscriber;

    @SpyBean
    @Autowired
    WebsocketsPublisherImpl websocketsPublisher;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @SneakyThrows
    void jokeIsSentToClients(boolean isNew) {
        //given
        val givenJoke = JokeObjectMother.getRandomJoke(1);
        val info = new EventInformationDto(givenJoke, isNew);
        Mockito.doNothing().when(websocketsPublisher).PublishJoke(givenJoke);

        //when
        websocketsSubscriber.update(info);

        //then
        Mockito.verify(websocketsPublisher).PublishJoke(givenJoke);
    }
}