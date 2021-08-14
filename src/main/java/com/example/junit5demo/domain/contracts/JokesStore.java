package com.example.junit5demo.domain.contracts;

import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.JokeAlreadyExistsException;
import lombok.NonNull;

import java.util.List;

public interface JokesStore {
    boolean existsJoke(long id);
    void addJoke(@NonNull JokeDto jokeDto) throws JokeAlreadyExistsException;
    List<JokeDto> getLast(int number);
}
