package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.notifiers.MessagePublisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeEvent;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.val;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class QueueSubscriberTest {

    @Mock
    MessagePublisher<JokeEvent> messagePublisher;

    @InjectMocks
    QueueSubscriber queueSubscriber;

    @Captor
    ArgumentCaptor<JokeEvent> captor;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void messageIsSentToNotifier(boolean isNew) {
        //given
        val joke = JokeObjectMother.getRandomJoke(1);
        val info = new EventInformationDto(joke, isNew);

        //when
        queueSubscriber.update(info);

        //then
        Mockito.verify(messagePublisher).publishEvent(captor.capture());
        val event = captor.getValue();
        assertEquals(String.valueOf(joke.getId()), event.getAggregateRootId());
        assertEquals(joke.getCategory(), event.getCategory());
        assertEquals(JokeEvent.EVENT_ID, event.getEventId());
    }

}