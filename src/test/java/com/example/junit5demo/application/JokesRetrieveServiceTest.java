package com.example.junit5demo.application;

import com.example.junit5demo.domain.contracts.JokesStore;
import com.example.junit5demo.domain.contracts.Publisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.feign.JokesService;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JokesRetrieveServiceTest {

    @Mock
    Publisher publisher;

    @Mock
    JokesService jokesService;

    @Mock
    JokesStore store;

    @InjectMocks
    JokesRetrieveService service;

    @Captor
    ArgumentCaptor<JokeDto> jokeDtoCaptor;

    @Captor
    ArgumentCaptor<EventInformationDto> eventInformationDtoArgumentCaptor;

    @Test
    @SneakyThrows
    void retrieveANewJokeCallsExternalServiceStoreAndPublisher() {
        //given
        val externalJoke = JokeObjectMother.getRandomExternalJoke(123);
        Mockito.when(jokesService.getRandomJoke()).thenReturn(externalJoke);
        Mockito.when(store.existsJoke(externalJoke.getId())).thenReturn(false);

        //when
        service.retrieveJoke();

        //then
        Mockito.verify(store).addJoke(jokeDtoCaptor.capture());
        Mockito.verify(publisher).notifySubscribers(eventInformationDtoArgumentCaptor.capture());

        val jokeSent = jokeDtoCaptor.getValue();
        assertEquals(externalJoke.getId(), jokeSent.getId());
        assertEquals(externalJoke.getType(), jokeSent.getCategory());
        assertEquals(externalJoke.getSetup(), jokeSent.getQuestion());
        assertEquals(externalJoke.getPunchline(), jokeSent.getAnswer());

        val eventSent = eventInformationDtoArgumentCaptor.getValue();
        assertTrue(eventSent.isNew());
        assertEquals(jokeSent, eventSent.getJoke());
    }

    @Test
    @SneakyThrows
    void retrieveAnExistingJokeCallsExternalServiceAndPublisher() {
        //given
        val externalJoke = JokeObjectMother.getRandomExternalJoke(123);
        Mockito.when(jokesService.getRandomJoke()).thenReturn(externalJoke);
        Mockito.when(store.existsJoke(externalJoke.getId())).thenReturn(true);

        //when
        service.retrieveJoke();

        //then
        Mockito.verify(publisher).notifySubscribers(eventInformationDtoArgumentCaptor.capture());

        val eventSent = eventInformationDtoArgumentCaptor.getValue();
        assertFalse(eventSent.isNew());

        val jokeSent = eventSent.getJoke();
        assertEquals(externalJoke.getId(), jokeSent.getId());
        assertEquals(externalJoke.getType(), jokeSent.getCategory());
        assertEquals(externalJoke.getSetup(), jokeSent.getQuestion());
        assertEquals(externalJoke.getPunchline(), jokeSent.getAnswer());
    }

    @Test
    @SneakyThrows
    void errorAccedingToExternalServiceWillNeitherCallStoreNorPublisher() {
        //given
        Mockito.when(jokesService.getRandomJoke()).thenReturn(null);

        //when
        service.retrieveJoke();

        //then
        Mockito.verifyNoInteractions(store);
        Mockito.verifyNoInteractions(publisher);
    }
}