package com.example.junit5demo.infrastructure.helpers;

import com.example.junit5demo.domain.dtos.ExternalJokeDto;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.infrastructure.jpa.JokeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConverterHelper {
    ConverterHelper MAPPER = Mappers.getMapper(ConverterHelper.class);

    @Mapping(target = "creationDate", ignore = true)
    JokeEntity dtoToEntity(JokeDto joke);

    JokeDto entityToDto(JokeEntity joke);

    @Mapping(source = "type", target = "category")
    @Mapping(source = "setup", target = "question")
    @Mapping(source = "punchline", target = "answer")
    JokeDto externalToDto(ExternalJokeDto joke);

    @Mapping(source = "type", target = "category")
    @Mapping(source = "setup", target = "question")
    @Mapping(source = "punchline", target = "answer")
    @Mapping(target = "creationDate", ignore = true)
    JokeEntity externalToEntity(ExternalJokeDto joke);
}
