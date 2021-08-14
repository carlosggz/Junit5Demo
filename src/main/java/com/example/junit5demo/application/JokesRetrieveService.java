package com.example.junit5demo.application;

import com.example.junit5demo.domain.contracts.JokesStore;
import com.example.junit5demo.domain.contracts.Publisher;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import com.example.junit5demo.domain.feign.JokesService;
import com.example.junit5demo.infrastructure.helpers.ConverterHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JokesRetrieveService {
    final Publisher publisher;
    final JokesService jokesService;
    final JokesStore store;

    public void retrieveJoke() {
        log.info("Retrieving joke...");
        try {
            val externalJoke = jokesService.getRandomJoke();
            val joke = ConverterHelper.MAPPER.externalToDto(externalJoke);
            val info = new EventInformationDto(joke, !store.existsJoke(joke.getId()));

            if (info.isNew()) {
                store.addJoke(joke);
            }

            publisher.notifySubscribers(info);
        }
        catch (Exception ex) {
            log.info("Exception retrieving and managing joke", ex);
        }
    }
}
