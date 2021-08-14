package com.example.junit5demo.integration;

import com.example.junit5demo.domain.contracts.Publisher;
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

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "app.schedule=*/10 * * * * *"
})
@Profile("integration")
public class ScheduledTaskTest extends WireMockBase {

    @SpyBean
    @Autowired
    Publisher publisher;

    @Autowired
    JokesRepository jokesRepository;

    @BeforeEach
    void setup() {
        startWiremockServer();
    }

    @AfterEach
    void cleanup() {
        stopWiremockServer();
    }

    @Test
    @SneakyThrows
    void scheduledTaskIsRunning() {
        //given
        Mockito.doNothing().when(publisher).notifySubscribers(Mockito.any());
        val givenObject = JokeObjectMother.getRandomExternalJoke(123);
        addStub("/random_joke", givenObject);
        Mockito.doNothing().when(publisher).notifySubscribers(Mockito.any());
        jokesRepository.deleteAll();

        //when/then
        await()
                .atMost(Duration.ofSeconds(20))
                .untilAsserted(() -> assertTrue(jokesRepository.count() > 0));
    }

}
