package com.example.junit5demo.integration;

import com.example.junit5demo.application.subscribers.RedisSubscriber;
import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import com.example.junit5demo.utils.TestRedisConfiguration;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestRedisConfiguration.class)
@Profile("integration")
class RedisSubscriberIntegrationTest {

    @Autowired
    @Qualifier("RedisSubscriber")
    private Subscriber redisSubscriber;

    @Autowired
    RedisTemplate<String, JokeDto> redisTemplate;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @SneakyThrows
    void jokeIsSentToRedis(boolean isNew) {
        //given
        val givenJoke = JokeObjectMother.getRandomJoke(1);
        val info = new EventInformationDto(givenJoke, isNew);

        //when
        redisSubscriber.update(info);

        //then
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            val actualValue = redisTemplate.opsForValue().get(RedisSubscriber.SUBSCRIBER_KEY);
            assertNotNull(actualValue);
            assertEquals(givenJoke, actualValue);
        });
    }
}