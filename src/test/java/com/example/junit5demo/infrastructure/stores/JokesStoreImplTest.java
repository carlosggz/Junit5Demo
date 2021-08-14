package com.example.junit5demo.infrastructure.stores;

import com.example.junit5demo.domain.exceptions.JokeAlreadyExistsException;
import com.example.junit5demo.infrastructure.helpers.ConverterHelper;
import com.example.junit5demo.infrastructure.jpa.JokesRepository;
import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class JokesStoreImplTest {

    @Autowired
    JokesRepository jokesRepository;

    JokesStoreImpl jokesStore;

    @BeforeEach
    void setup() {
        jokesStore = new JokesStoreImpl(jokesRepository);
        jokesRepository.deleteAll();
    }

    @Test
    void existsJokeReturnsFalseWhenTheJokeDoesNotExists() {
        assertFalse(jokesStore.existsJoke(123));
    }

    @Test
    void existsJokeReturnsTrueWhenTheJokeExists() {
        //given
        val id = 123L;
        val dto = JokeObjectMother.getRandomJoke(id);
        jokesRepository.save(ConverterHelper.MAPPER.dtoToEntity(dto));

        //when/then
        assertTrue(jokesStore.existsJoke(id));
    }

    @Test
    void addNullJokeThrowsException() {
        assertThrows(NullPointerException.class, () -> jokesStore.addJoke(null));
    }

    @Test
    void addExistingJokeThrowsException() {
        //given
        val dto = JokeObjectMother.getRandomJoke(123L);
        jokesRepository.save(ConverterHelper.MAPPER.dtoToEntity(dto));

        //when
        assertThrows(JokeAlreadyExistsException.class, () -> jokesStore.addJoke(dto));
    }

    @Test
    @SneakyThrows
    void addNewJokeIsAdded() {
        //given
        val id = 123L;
        val dto = JokeObjectMother.getRandomJoke(id);

        //when
        jokesStore.addJoke(dto);

        //then
        assertEquals(1, jokesRepository.count());

        val entity = jokesRepository.findById(id);
        assertTrue(entity.isPresent());
        assertEquals(dto, ConverterHelper.MAPPER.entityToDto(entity.get()));
    }

    @ParameterizedTest(name = "[{0}]")
    @MethodSource("getJokesArguments")
    void getLastReturnTheLatestAdded(String name, int max, int toGet, int expected) {
        //given
        IntStream.rangeClosed(1, max).boxed()
                .map(JokeObjectMother::getRandomJoke)
                .forEach(x -> jokesRepository.save(ConverterHelper.MAPPER.dtoToEntity(x)));

        //when
        val items = jokesStore.getLast(toGet);

        //then
        assertNotNull(items);
        assertEquals(expected, items.size());

        var current = max;

        for (val item: items) {
            assertEquals(current, item.getId());
            current--;
        }
    }

    static Stream<Arguments> getJokesArguments() {
        return Stream.of(
                Arguments.of("When add more than request, then return the requested", 20, 10, 10),
                Arguments.of("When add the same than request, then return the requested", 10, 10, 10),
                Arguments.of("When add less than request, then return the existing", 5, 10, 5),
                Arguments.of("When there are no items, then return nothing", 0, 10, 0)
        );
    }
}