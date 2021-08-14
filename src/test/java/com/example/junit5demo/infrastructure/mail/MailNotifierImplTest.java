package com.example.junit5demo.infrastructure.mail;

import com.example.junit5demo.domain.exceptions.MailNotifierException;
import com.example.junit5demo.infrastructure.config.MailProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MailNotifierImplTest {
    @Mock
    MailProperties mailProperties;

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    MailNotifierImpl mailNotifier;

    @Mock
    MimeMessage mimeMessage;

    static String givenSubject = "subject";
    static String givenFromName = "from name";
    static String givenFromAddress = "from@example.com";
    static String givenTo = "to1@example.com,to2@example.com";
    static String givenBcc = "bcc@example.com,bcc2@example.com";
    static String expectedBody = "message body";

    @BeforeEach
    void setup(){
        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        Mockito.when(mailProperties.getSubject()).thenReturn(givenSubject);
        Mockito.when(mailProperties.getFromAddress()).thenReturn(givenFromAddress);
        Mockito.when(mailProperties.getFromName()).thenReturn(givenFromName);
        Mockito.when(mailProperties.getToAddress()).thenReturn(Arrays.stream(givenTo.split(",")).collect(Collectors.toList()));
        Mockito.when(mailProperties.getBccAddress()).thenReturn(Arrays.stream(givenBcc.split(",")).collect(Collectors.toList()));
    }

    @Test
    @SneakyThrows
    void whenEmailIsSent(){
        //when
        mailNotifier.sendNotification(expectedBody);

        //then
        Mockito.verify(mailSender).send(mimeMessage);
        Mockito.verify(mimeMessage).setSubject(givenSubject, "UTF-8");
        Mockito.verify(mimeMessage).setFrom(new InternetAddress(givenFromAddress, givenFromName));
        Mockito.verify(mimeMessage).setRecipients(Message.RecipientType.TO, InternetAddress.parse(givenTo));
        Mockito.verify(mimeMessage).setRecipients(Message.RecipientType.BCC, InternetAddress.parse(givenBcc));
        Mockito.verify(mimeMessage).setContent(expectedBody, "text/html;charset=UTF-8");
    }

    @Test
    void whenErrorIsProducedMailNotifierExceptionIsThrow(){
        //given
        Mockito.doThrow(new MailSendException("Custom message")).when(mailSender).send(mimeMessage);

        //when/then
        assertThrows(MailNotifierException.class, () -> mailNotifier.sendNotification(expectedBody));
    }
}