package com.example.junit5demo.integration;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.infrastructure.config.FtpProperties;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Profile("integration")
class FtpSubscriberIntegrationTest {

    final static String HOME_FOLDER = "/data";

    @Autowired
    FtpProperties ftpProperties;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("FtpSubscriber")
    private Subscriber ftpSubscriber;

    FakeFtpServer fakeFtpServer;

    @BeforeEach
    void setup() {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount(ftpProperties.getUserName(), ftpProperties.getPassword(), HOME_FOLDER));

        val fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry(HOME_FOLDER));

        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(ftpProperties.getPort());
        fakeFtpServer.start();
    }

    @AfterEach
    void teardown(){
        fakeFtpServer.stop();
    }

    @Test
    @SneakyThrows
    void whenJokeIsNotNewFileWillNotBeUploaded(){
        //given
        val info = new EventInformationDto(JokeObjectMother.getRandomJoke(123), false);

        //when
        ftpSubscriber.update(info);

        //then

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            val fs = fakeFtpServer.getFileSystem();
            val files = fs.listNames(HOME_FOLDER);
            assertEquals(0, files.size());
        });
    }

    @Test
    @SneakyThrows
    void whenJokeIsNewFileWillBeUploaded(){
        //given
        val info = new EventInformationDto(JokeObjectMother.getRandomJoke(123), true);
        val expectedName = String.format("joke%s.json", info.getJoke().getId());
        val expectedFileContent = objectMapper.writeValueAsString(info.getJoke());

        //when
        ftpSubscriber.update(info);

        //then

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            val fs = fakeFtpServer.getFileSystem();
            val files = fs.listNames(HOME_FOLDER);
            assertEquals(1, files.size());
            assertEquals(expectedName, files.get(0));

            val fileEntry = (FileEntry)fs.getEntry(HOME_FOLDER + "/" + expectedName);
            val inputStream = fileEntry.createInputStream();
            val actualContent = String.join("", IOUtils.readLines(inputStream, StandardCharsets.UTF_8));
            assertEquals(expectedFileContent, actualContent);
        });
    }
}