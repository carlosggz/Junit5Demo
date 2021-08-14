package com.example.junit5demo.integration;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.infrastructure.config.MailProperties;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Profile("integration")
class MailSubscriberIntegrationTest {

    @Autowired
    MailProperties mailProperties;

    @Autowired
    @Qualifier("MailSubscriber")
    private Subscriber mailSubscriber;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "test"));

    @Test
    void whenJokeIsNotNewEmailsAreNotSent(){
        //given
        val info = new EventInformationDto(JokeObjectMother.getRandomJoke(123), false);

        //when
        mailSubscriber.update(info);

        //then
        greenMail.waitForIncomingEmail(1000, getExpectedMails());
        assertEquals(0, greenMail.getReceivedMessages().length);
    }

    @Test
    void whenJokeIsNewEmailsAreSent(){
        //given
        val info = new EventInformationDto(JokeObjectMother.getRandomJoke(123), true);
        val expectedMails = getExpectedMails();

        //when
        mailSubscriber.update(info);

        //then
        await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            val emails = greenMail.getReceivedMessages();
            assertEquals(expectedMails, emails.length);
        });
    }

    int getExpectedMails() {
        return mailProperties.getToAddress().size() +
                Optional.ofNullable(mailProperties.getBccAddress()).map(List::size).orElse(0);
    }
}