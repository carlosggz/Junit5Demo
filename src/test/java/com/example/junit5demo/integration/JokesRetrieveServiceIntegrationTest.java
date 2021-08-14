package com.example.junit5demo.integration;

import com.example.junit5demo.application.JokesRetrieveService;
import com.example.junit5demo.domain.contracts.Publisher;
import com.example.junit5demo.infrastructure.helpers.ConverterHelper;
import com.example.junit5demo.infrastructure.jpa.JokesRepository;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import com.example.junit5demo.utils.WireMockBase;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Profile("integration")
class JokesRetrieveServiceIntegrationTest extends WireMockBase {

    @SpyBean
    @Autowired
    Publisher publisher;

    @Autowired
    JokesRetrieveService service;

    @Autowired
    JokesRepository jokesRepository;

    @BeforeEach
    void setup() {
        startWiremockServer();
        jokesRepository.deleteAll();
    }

    @AfterEach
    void cleanup() {
        stopWiremockServer();
    }

    @Test
    @SneakyThrows
    public void retrieveNewJokeStoresItOnDbAndCallsPublisher() {
        //given
        val givenObject = JokeObjectMother.getRandomExternalJoke(123);
        addStub("/random_joke", givenObject);
        Mockito.doNothing().when(publisher).notifySubscribers(Mockito.any());

        //when
        service.retrieveJoke();

        //then
        assertEquals(1, jokesRepository.count());

        val joke = jokesRepository.findById(givenObject.getId());
        assertTrue(joke.isPresent());
        assertEquals(givenObject.getType(), joke.get().getCategory());
        assertEquals(givenObject.getSetup(), joke.get().getQuestion());
        assertEquals(givenObject.getPunchline(), joke.get().getAnswer());

        Mockito.verify(publisher).notifySubscribers(Mockito.any());
    }

    @Test
    @SneakyThrows
    public void retrieveExistingJokeCallsPublisherOnly() {
        //given
        val givenObject = JokeObjectMother.getRandomExternalJoke(123);
        jokesRepository.save(ConverterHelper.MAPPER.externalToEntity(givenObject));
        addStub("/random_joke", givenObject);
        Mockito.doNothing().when(publisher).notifySubscribers(Mockito.any());

        //when
        service.retrieveJoke();

        //then
        assertEquals(1, jokesRepository.count());

        val joke = jokesRepository.findById(givenObject.getId());
        assertTrue(joke.isPresent());
        assertEquals(givenObject.getType(), joke.get().getCategory());
        assertEquals(givenObject.getSetup(), joke.get().getQuestion());
        assertEquals(givenObject.getPunchline(), joke.get().getAnswer());

        Mockito.verify(publisher).notifySubscribers(Mockito.any());
    }
}