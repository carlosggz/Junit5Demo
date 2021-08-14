package com.example.junit5demo.domain.feign;

import com.example.junit5demo.domain.dtos.ExternalJokeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(url = "${app.jokes-url}", name = "JokesServiceClient")
public interface JokesService {

    @GetMapping(value = "/random_joke", produces = APPLICATION_JSON_VALUE)
    ExternalJokeDto getRandomJoke();
}
