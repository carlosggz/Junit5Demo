package com.example.junit5demo.infrastructure.controllers;

import com.example.junit5demo.application.JokesQueryService;
import com.example.junit5demo.domain.dtos.JokeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("jokes")
@RequiredArgsConstructor
public class JokesController {
    private static final int DEFAULT_JOKES = 10;
    private final JokesQueryService jokesQueryService;

    @GetMapping({"{number}", ""})
    public List<JokeDto> getJokes(@PathVariable(required = false, name = "number") Optional<Integer> number) {
        return jokesQueryService.getLastJokes(number.orElse(DEFAULT_JOKES));
    }

}
