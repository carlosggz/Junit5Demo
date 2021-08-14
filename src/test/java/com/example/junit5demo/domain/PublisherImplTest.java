package com.example.junit5demo.domain;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeDto;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PublisherImplTest {

    PublisherImpl publisher;

    @BeforeEach
    void setup() {
        publisher = new PublisherImpl();
    }

    @ParameterizedTest(name = "[{0}]")
    @MethodSource("addSubscriberArguments")
    void addSubscriber(String testName, List<Subscriber> subscribers, int expectedSubscribers) {
        //when
        subscribers.forEach(x -> publisher.addSubscriber(x));

        //then
        assertEquals(expectedSubscribers, publisher.totalOfSubscribers());
    }

    @Test
    @DisplayName("Add null subscriber ignores it")
    void addNullSubscriberIgnoresIt() {
        //when
        publisher.addSubscriber(null);

        //then
        assertEquals(0, publisher.totalOfSubscribers());
    }

    @Test
    @DisplayName("When a subscriber is added then he is on the list")
    void addedSubscriberIsPresent() {
        //given
        val subscribers = List.of(
                Mockito.mock(Subscriber.class),
                Mockito.mock(Subscriber.class),
                Mockito.mock(Subscriber.class)
        );

        //when
        subscribers.forEach(x -> publisher.addSubscriber(x));

        //then
        publisher.getSubscribers().forEach(x -> assertTrue(subscribers.contains(x)));
    }

    @Test
    @DisplayName("Remove a subscriber let the rest")
    void removeSubscriber() {
        //given
        val subscriber1 = Mockito.mock(Subscriber.class);
        val subscriber2 = Mockito.mock(Subscriber.class);
        val subscriber3 = Mockito.mock(Subscriber.class);
        Stream
                .of(subscriber1, subscriber2, subscriber3)
                .forEach(x -> publisher.addSubscriber(x));

        //when/then
        publisher.removeSubscriber(null);
        assertEquals(3, publisher.totalOfSubscribers());

        publisher.removeSubscriber(subscriber2);
        assertEquals(2, publisher.totalOfSubscribers());

        val currentSubscribers = publisher.getSubscribers();
        assertTrue(currentSubscribers.contains(subscriber1));
        assertTrue(currentSubscribers.contains(subscriber3));
    }

    @Test
    @DisplayName("Remove a non added subscriber does nothing")
    void removeNonAddedSubscriberDoesNothing() {
        //given
        val subscriber1 = Mockito.mock(Subscriber.class);
        val subscriber2 = Mockito.mock(Subscriber.class);
        val nonAddedSubscriber = Mockito.mock(Subscriber.class);
        Stream
                .of(subscriber1, subscriber2)
                .forEach(x -> publisher.addSubscriber(x));

        //when/then
        publisher.removeSubscriber(null);
        assertEquals(2, publisher.totalOfSubscribers());

        publisher.removeSubscriber(nonAddedSubscriber);
        assertEquals(2, publisher.totalOfSubscribers());

        val currentSubscribers = publisher.getSubscribers();
        assertTrue(currentSubscribers.contains(subscriber1));
        assertTrue(currentSubscribers.contains(subscriber2));
    }

    @Test
    @DisplayName("When notify method is called then each subscriber is notified")
    void notifySubscriberTest() {
        //given
        val subscribers = IntStream
                .rangeClosed(1, 5)
                .boxed()
                .map(x -> Mockito.mock(Subscriber.class))
                .collect(Collectors.toList());
        subscribers.forEach(x -> publisher.addSubscriber(x));

        val info = new EventInformationDto(new JokeDto(), true);

        //when
        publisher.notifySubscribers(info);

        //then
        subscribers.forEach(x -> Mockito.verify(x).update(info));
    }

    static Stream<Arguments> addSubscriberArguments() {
        val subscriber1 = Mockito.mock(Subscriber.class);
        val subscriber2 = Mockito.mock(Subscriber.class);

        return Stream.of(
                Arguments.of("Empty does nothing", List.of(), 0),
                Arguments.of("Single subscriber add 1", List.of(subscriber1), 1),
                Arguments.of("Several subscribers add all of them", List.of(subscriber1, subscriber2), 2),
                Arguments.of("Same subscriber is only once case 1", List.of(subscriber1, subscriber1, subscriber1), 1),
                Arguments.of("Same subscriber is only once case 2", List.of(subscriber1, subscriber2, subscriber1), 2)
        );
    }
}