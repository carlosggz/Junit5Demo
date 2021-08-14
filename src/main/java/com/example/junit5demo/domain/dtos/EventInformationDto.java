package com.example.junit5demo.domain.dtos;

import lombok.NonNull;
import lombok.Value;

@Value
public class EventInformationDto {
    @NonNull
    JokeDto joke;

    boolean isNew;
}
