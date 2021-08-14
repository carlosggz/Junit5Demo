package com.example.junit5demo.infrastructure.stores;

import com.example.junit5demo.domain.contracts.JokesStore;
import com.example.junit5demo.domain.dtos.JokeDto;
import com.example.junit5demo.domain.exceptions.JokeAlreadyExistsException;
import com.example.junit5demo.infrastructure.helpers.ConverterHelper;
import com.example.junit5demo.infrastructure.jpa.JokeEntity;
import com.example.junit5demo.infrastructure.jpa.JokesRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JokesStoreImpl implements JokesStore {
    private final JokesRepository jokesRepository;

    @Override
    public boolean existsJoke(long id) {
        return jokesRepository.existsById(id);
    }

    @Override
    public void addJoke(@NonNull JokeDto jokeDto) throws JokeAlreadyExistsException {
        if (existsJoke(jokeDto.getId())) {
            throw new JokeAlreadyExistsException();
        }

        jokesRepository.save(ConverterHelper.MAPPER.dtoToEntity(jokeDto));
    }

    @Override
    public List<JokeDto> getLast(int number) {
        Pageable top = PageRequest.of(0, number, Sort.Direction.DESC, "creationDate", "id");
        Page<JokeEntity> results = jokesRepository.findAll(top);
        return results.getTotalElements() == 0
                ? List.of()
                : results
                    .getContent()
                    .stream()
                    .map(ConverterHelper.MAPPER::entityToDto)
                    .collect(Collectors.toList());
    }
}
