package com.example.junit5demo.domain.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class JokeEvent extends BaseEvent {
    public static final String EVENT_ID = "joke.event.register";
    String category;

    public JokeEvent(String aggregateRootId, String category) {
        super(EVENT_ID, aggregateRootId);
        this.category = category;
    }
}
