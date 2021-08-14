package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.notifiers.FtpPublisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.FtpNotifierException;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FtpSubscriberTest {

    @Mock
    FtpPublisher ftpPublisher;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    FtpSubscriber ftpSubscriber;

    @Test
    @SneakyThrows
    void whenJokeIsNotNewFileWillNotBeUploaded(){
        //given
        val info = new EventInformationDto(JokeObjectMother.getRandomJoke(123), false);

        //when
        ftpSubscriber.update(info);

        //then
        Mockito.verifyNoInteractions(ftpPublisher);
    }

    @Test
    @SneakyThrows
    void whenJokeIsNewFileWillBeUploaded(){
        //given
        val info = new EventInformationDto(JokeObjectMother.getRandomJoke(123), true);
        Mockito.when(objectMapper.writeValueAsString(info.getJoke())).thenReturn("test");

        //when
        ftpSubscriber.update(info);

        //then
        Mockito.verify(ftpPublisher).publishFile(Mockito.any(), Mockito.any());
    }

    @Test
    @SneakyThrows
    void whenFtpPublisherThrowsExceptionItIsCaught(){
        //given
        val joke = JokeObjectMother.getRandomJoke(123);
        val info = new EventInformationDto(joke, true);
        Mockito.when(objectMapper.writeValueAsString(info.getJoke())).thenReturn("test");
        Mockito.doThrow(FtpNotifierException.class).when(ftpPublisher).publishFile(Mockito.any(), Mockito.any());

        //when/then
        ftpSubscriber.update(info);
    }

    @Test
    @SneakyThrows
    void whenObjectMapperThrowsExceptionItIsCaught(){
        //given
        val joke = JokeObjectMother.getRandomJoke(123);
        val info = new EventInformationDto(joke, true);
        Mockito.when(objectMapper.writeValueAsString(info.getJoke())).thenThrow(JsonProcessingException.class);

        //when
        ftpSubscriber.update(info);

        //then
        Mockito.verifyNoInteractions(ftpPublisher);
    }
}