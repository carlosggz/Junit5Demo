package com.example.junit5demo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JokeDto implements Serializable {
    private long id;
    private String category;
    private String question;
    private String answer;
}
