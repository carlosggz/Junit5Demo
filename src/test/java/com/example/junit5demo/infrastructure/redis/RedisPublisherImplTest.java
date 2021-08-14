package com.example.junit5demo.infrastructure.redis;

import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.RedisNotifierException;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import com.example.junit5demo.utils.TestRedisConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRedisConfiguration.class)
class RedisPublisherImplTest {

    static final String TEST_KEY = "myKey";

    @Autowired
    RedisTemplate<String, JokeDto> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RedisPublisherImpl redisPublisher;

    @BeforeEach
    void clear() {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(TEST_KEY))) {
            redisTemplate.delete(TEST_KEY);
        }
    }

    @Test
    @SneakyThrows
    void publishAddTheValueToRedis() {
        //given
        val givenDto = JokeObjectMother.getRandomJoke(123);

        //when
        redisPublisher.publish(TEST_KEY, givenDto);

        //then
        val actualValue = redisTemplate.opsForValue().get(TEST_KEY);
        assertNotNull(actualValue);
        assertEquals(givenDto, actualValue);
    }

    @Test
    @SneakyThrows
    void existingValueReturnsTheObject() {
        //given
        val givenDto = JokeObjectMother.getRandomJoke(123);
        redisTemplate.opsForValue().set(TEST_KEY, givenDto);

        //when
        val actualValue = redisPublisher.get(TEST_KEY);

        //then
        assertTrue(actualValue.isPresent());
        assertEquals(givenDto, actualValue.get());
    }

    @Test
    @SneakyThrows
    void nonExistingValueReturnsAnEmptyObject() {
        //when
        val actualValue = redisPublisher.get(TEST_KEY);

        //then
        assertTrue(actualValue.isEmpty());
    }

    @Test
    @SneakyThrows
    void putAndGetReturnTheCorrectValue() {
        //given
        val givenDto = JokeObjectMother.getRandomJoke(123);

        //when
        redisPublisher.publish(TEST_KEY, givenDto);
        val actualDto = redisPublisher.get(TEST_KEY);

        //then
        assertTrue(actualDto.isPresent());
        assertEquals(givenDto, actualDto.get());
    }

    @Test
    @SneakyThrows
    void invalidValuesWillThrowException() {
        assertThrows(RedisNotifierException.class, () -> redisPublisher.publish(null, null));
        assertThrows(RedisNotifierException.class, () -> redisPublisher.get(null));
    }
}