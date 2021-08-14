package com.example.junit5demo.application.subscribers;

import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.contracts.notifiers.FtpPublisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("FtpSubscriber")
public class FtpSubscriber implements Subscriber {
    final FtpPublisher ftpPublisher;
    final ObjectMapper objectMapper;

    @Override
    @Async("taskExecutor")
    public void update(@NonNull EventInformationDto eventInformationDto) {

        log.info("Inside ftp subscriber");

        if (eventInformationDto.isNew()) {
            uploadFile(eventInformationDto);
        }
        else {
            log.info("Joke already existed, ftp file won't be uploaded again");
        }

        log.info("Exiting ftp subscriber");
    }

    private void uploadFile(EventInformationDto eventInformationDto) {
        try {
            final String fileName = String.format("joke%s.json", eventInformationDto.getJoke().getId());
            log.info("Uploading file {}...", fileName);
            final String fileContent = objectMapper.writeValueAsString(eventInformationDto.getJoke());
            ftpPublisher.publishFile(fileName, IOUtils.toInputStream(fileContent));
            log.info("File uploaded successfully");
        }
        catch(JsonProcessingException ex) {
            log.error("Error processing file content", ex);
        }
        catch(Exception ex) {
            log.error("Error processing ftp publish", ex);
        }
    }
}
