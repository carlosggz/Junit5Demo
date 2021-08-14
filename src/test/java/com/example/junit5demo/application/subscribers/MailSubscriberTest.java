package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.notifiers.MailNotifier;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.MailNotifierException;
import com.example.junit5demo.infrastructure.helpers.TemplatesHelper;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MailSubscriberTest {

    @Mock
    MailNotifier mailNotifier;

    @InjectMocks
    MailSubscriber mailSubscriber;

    @Test
    void whenJokeIsNotNewEmailWillNotBeSent(){
        //given
        val info = new EventInformationDto(JokeObjectMother.getRandomJoke(123), false);

        //when
        mailSubscriber.update(info);

        //then
        Mockito.verifyNoInteractions(mailNotifier);
    }

    @Test
    @SneakyThrows
    void whenJokeIsNewEmailsWillBeSent(){
        //given
        val joke = JokeDto.builder().id(1L).category("general").question("question").answer("answer").build();
        val info = new EventInformationDto(joke, true);
        val expectedBody = TemplatesHelper.getContentFromTemplate("templates/expected-mail.html");

        //when
        mailSubscriber.update(info);

        //then
        Mockito.verify(mailNotifier).sendNotification(expectedBody);
    }

    @Test
    @SneakyThrows
    void whenMailNotifierThrowsExceptionItIsCaught(){
        //given
        val joke = JokeObjectMother.getRandomJoke(123);
        val info = new EventInformationDto(joke, true);
        Mockito.doThrow(MailNotifierException.class).when(mailNotifier).sendNotification(Mockito.any());

        //when/then
        mailSubscriber.update(info);
    }
}