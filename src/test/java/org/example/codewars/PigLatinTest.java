package org.example.codewars;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PigLatinTest {

    @ParameterizedTest
    @MethodSource
    void pigayTest(String given, String expected) {
        String actual = PigLatin.pigay(given);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> pigayTest() {
        return Stream.of(
                Arguments.of("test", "esttay"),
                Arguments.of("Pig", "igPay"),
                Arguments.of("latin", "atinlay"),
                Arguments.of("is", "siay"),
                Arguments.of("cool", "oolcay"),
                Arguments.of("This", "hisTay")
        );
    }

    @ParameterizedTest
    @MethodSource
    void pigItTest(String given, String expected) {
        String actual = PigLatin.pigIt(given);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> pigItTest() {
        return Stream.of(
                Arguments.of("Test", "estTay"),
                Arguments.of("Pig latin is cool", "igPay atinlay siay oolcay"),
                Arguments.of("This is my string", "hisTay siay ymay tringsay")
        );

    }
}
