package com.example.junit5demo.infrastructure.controllers;

import com.example.junit5demo.application.JokesQueryService;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JokesControllerTest {

    @Mock
    JokesQueryService jokesQueryService;

    @InjectMocks
    JokesController jokesController;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(jokesController)
                .build();
    }

    @Test
    void getLastItemsReturnsAList() throws Exception {

        val quantity = 5;
        val items = IntStream.rangeClosed(1, 3)
                .boxed()
                .map(JokeObjectMother::getRandomJoke)
                .collect(Collectors.toList());
        when(jokesQueryService.getLastJokes(quantity)).thenReturn(items);

        mockMvc
                .perform(get("/jokes/" + quantity)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(items.size())));
    }

    @Test
    void getLastItemsWithoutAQuantityReturns10ByDefault() throws Exception {

        val quantity = 10;
        val items = IntStream
                .rangeClosed(1, quantity)
                .boxed()
                .map(JokeObjectMother::getRandomJoke)
                .collect(Collectors.toList());
        when(jokesQueryService.getLastJokes(quantity)).thenReturn(items);

        mockMvc
                .perform(get("/jokes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(quantity)));
    }
}