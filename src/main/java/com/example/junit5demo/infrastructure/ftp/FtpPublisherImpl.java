package com.example.junit5demo.infrastructure.ftp;

import com.example.junit5demo.domain.contracts.notifiers.FtpPublisher;
import com.example.junit5demo.domain.exceptions.FtpNotifierException;
import com.example.junit5demo.infrastructure.config.FtpProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class FtpPublisherImpl implements FtpPublisher {
    final FtpProperties ftpProperties;

    @Override
    public void publishFile(String fileName, InputStream fileContent) throws FtpNotifierException {

        try {
            FTPClient ftp = new FTPClient();
            ftp.connect(ftpProperties.getServerName(), ftpProperties.getPort());
            ftp.login(ftpProperties.getUserName(), ftpProperties.getPassword());
            ftp.enterLocalPassiveMode();
            ftp.storeFile(fileName, fileContent);
            ftp.disconnect();
        }
        catch (Exception ex) {
            throw new FtpNotifierException("Error processing file on ftp", ex);
        }
    }
}
