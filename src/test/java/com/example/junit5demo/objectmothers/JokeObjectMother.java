package com.example.junit5demo.objectmothers;

import com.example.junit5demo.domain.dtos.ExternalJokeDto;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JokeObjectMother {
    private static final Faker faker = new Faker();

    public JokeDto getRandomJoke(long id) {
        return JokeDto.builder()
                .id(id)
                .category(faker.book().genre())
                .question(faker.book().title())
                .answer(faker.book().author())
                .build();
    }

    public ExternalJokeDto getRandomExternalJoke(long id) {
        return new ExternalJokeDto(
                id,
                faker.book().genre(),
                faker.book().title(),
                faker.book().author()
        );
    }
}
