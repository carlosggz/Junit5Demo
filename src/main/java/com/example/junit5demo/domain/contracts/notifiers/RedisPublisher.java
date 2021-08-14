package com.example.junit5demo.domain.contracts.notifiers;

import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.RedisNotifierException;

import java.util.Optional;

public interface RedisPublisher {
    void publish(String key, JokeDto value) throws RedisNotifierException;
    Optional<JokeDto> get(String key) throws RedisNotifierException;
}
