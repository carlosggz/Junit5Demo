package com.example.junit5demo.infrastructure.helpers;

import com.example.junit5demo.objectmothers.JokeObjectMother;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplatesHelperTest {

    @Test
    @SneakyThrows
    void getContentFromResourceReturnsTheContent() {
        //given
        val expectedResult = "Testing content.\n" +
                "\n" +
                "{{joke.category}}\n" +
                "\n" +
                "end of file\n";

        //when
        val content = TemplatesHelper.getContentFromTemplate("templates/email.txt");

        //then
        assertEquals(expectedResult, content.replace("\r", ""));
    }

    @Test
    @SneakyThrows
    void getRenderedTemplateReturnsTheContentWithTheMappedValues() {
        //given
        val joke = JokeObjectMother.getRandomJoke(123);
        val expectedResult = "Testing content.\n" +
                "\n" +
                joke.getCategory() + "\n" +
                "\n" +
                "end of file\n";

        //when
        val content = TemplatesHelper.getRenderedTemplate("templates/email.txt", Map.of("joke", joke));

        //then
        assertEquals(expectedResult, content.replace("\r", ""));
    }
}