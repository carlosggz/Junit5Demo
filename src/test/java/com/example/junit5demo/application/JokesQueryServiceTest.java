package com.example.junit5demo.application;

import com.example.junit5demo.domain.contracts.JokesStore;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JokesQueryServiceTest {

    @Mock
    JokesStore jokesStore;

    @InjectMocks
    JokesQueryService jokesQueryService;

    @Test
    void getWillCallTheStore() {
        //given
        val quantity = 10;
        val expectedJokes = IntStream.of(1,2,3)
                .boxed()
                .map(JokeObjectMother::getRandomJoke)
                .collect(Collectors.toList());
        Mockito.when(jokesStore.getLast(quantity)).thenReturn(expectedJokes);

        //when
        val actualResult = jokesQueryService.getLastJokes(quantity);

        //then
        assertTrue(expectedJokes.containsAll(actualResult));
    }

    @Test
    void getWithAnInvalidQuantityReturnsLastRegister() {
        //given
        val expectedJoke = JokeObjectMother.getRandomJoke(1);
        Mockito.when(jokesStore.getLast(1)).thenReturn(List.of(expectedJoke));

        //when
        val actualResult = jokesQueryService.getLastJokes(-1);

        //then
        assertEquals(1, actualResult.size());
        assertEquals(expectedJoke, actualResult.get(0));
    }
}