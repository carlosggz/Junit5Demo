package com.example.junit5demo.application;

import com.example.junit5demo.domain.contracts.JokesStore;
import com.example.junit5demo.domain.dtos.JokeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JokesQueryService {
    final JokesStore store;

    public List<JokeDto> getLastJokes(int number) {
        return store.getLast(Math.max(1, number));
    }
}
