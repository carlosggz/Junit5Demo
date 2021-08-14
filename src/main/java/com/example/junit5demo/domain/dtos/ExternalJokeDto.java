package com.example.junit5demo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalJokeDto {
    private long id;
    private String type;
    private String setup;
    private String punchline;
}
