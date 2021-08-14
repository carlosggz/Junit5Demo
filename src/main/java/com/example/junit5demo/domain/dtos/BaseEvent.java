package com.example.junit5demo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class BaseEvent implements Serializable {
    String eventId;
    String aggregateRootId;
}
