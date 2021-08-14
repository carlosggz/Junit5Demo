package com.example.junit5demo.infrastructure.config;

import com.example.junit5demo.application.JokesRetrieveService;
import com.example.junit5demo.domain.contracts.Publisher;
import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

@Configuration
public class ApplicationConfig {

    private final JokesRetrieveService jokesRetrieveService;
    private final ObjectMapper objectMapper;

    public ApplicationConfig(
            Publisher publisher,
            List<Subscriber> subscribers,
            JokesRetrieveService jokesRetrieveService,
            ObjectMapper objectMapper) {
        this.jokesRetrieveService = jokesRetrieveService;
        this.objectMapper = objectMapper;
        subscribers.forEach(publisher::addSubscriber);
    }

    @Scheduled(cron="${app.schedule:-}")
    void retrieveJokes() {
        jokesRetrieveService.retrieveJoke();
    }

    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean
    public RedisTemplate<String, JokeDto> redisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<JokeDto> valueSerializer = new Jackson2JsonRedisSerializer<>(JokeDto.class);
        valueSerializer.setObjectMapper(objectMapper);

        RedisTemplate<String, JokeDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(valueSerializer);
        template.setDefaultSerializer(valueSerializer);

        template.afterPropertiesSet();

        return template;
    }
}
