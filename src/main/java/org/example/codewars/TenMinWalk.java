package org.example.codewars;

import java.util.Arrays;
import java.util.stream.IntStream;

public class TenMinWalk {
    public static boolean isValid(char[] walk) {
        if (walk.length != 10) {
            return false;
        }

        return countCharIn(walk, 'n') == countCharIn(walk, 's') &&
                countCharIn(walk, 'e') == countCharIn(walk, 'w');
    }

    private static int countCharIn(char[] array, char c) {
        int count = 0;
        for (char i : array) {
            if (i == c) {
                count++;
            }
        }
        return count;
    }
}
