package com.example.junit5demo.infrastructure.ftp;

import com.example.junit5demo.domain.exceptions.FtpNotifierException;
import com.example.junit5demo.infrastructure.config.FtpProperties;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FtpPublisherImplTest {

    final static String HOME_FOLDER = "/data";

    @Autowired
    FtpProperties ftpProperties;

    @Autowired
    FtpPublisherImpl ftpPublisher;

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
    void uploadFileWillCreateANewFile() {
        //given
        val fileName = "joke123.json";
        val fileContent = "{ \"id\": 123 }";

        //when
        ftpPublisher.publishFile(fileName, IOUtils.toInputStream(fileContent, StandardCharsets.UTF_8));

        //then
        val fs = fakeFtpServer.getFileSystem();
        val files = fs.listNames(HOME_FOLDER);
        assertEquals(1, files.size());
        assertEquals(fileName, files.get(0));

        val fileEntry = (FileEntry)fs.getEntry(HOME_FOLDER + "/" + fileName);
        val inputStream = fileEntry.createInputStream();
        val actualContent = String.join("", IOUtils.readLines(inputStream, StandardCharsets.UTF_8));
        assertEquals(fileContent, actualContent);
    }

    @Test
    @SneakyThrows
    void invalidParametersWillThrowException() {
        assertThrows(FtpNotifierException.class, () -> ftpPublisher.publishFile(HOME_FOLDER + "/test.txt", null));
    }

}