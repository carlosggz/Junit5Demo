package com.example.junit5demo.infrastructure.redis;

import com.example.junit5demo.domain.contracts.notifiers.RedisPublisher;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.RedisNotifierException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisPublisherImpl implements RedisPublisher {

    final private long seconds;
    final RedisTemplate<String, JokeDto> redisTemplate;

    public RedisPublisherImpl(
            @Lazy RedisTemplate<String, JokeDto> redisTemplate, //important
            @Value(value = "${app.redisSeconds:60}") int seconds) {
        this.redisTemplate = redisTemplate;
        this.seconds = seconds;
    }

    @Override
    public void publish(String key, JokeDto value) throws RedisNotifierException {
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        }
        catch (Exception ex){
            throw new RedisNotifierException("Error writing message on Redis", ex);
        }
    }

    @Override
    public Optional<JokeDto> get(String key) throws RedisNotifierException {
        try {
            return Optional.ofNullable(redisTemplate.opsForValue().get(key));
        }
        catch (Exception ex) {
            throw new RedisNotifierException("Error reading message on Redis", ex);
        }
    }
}
