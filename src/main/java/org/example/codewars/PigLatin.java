package org.example.codewars;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PigLatin {
    protected static Pattern WORD = Pattern.compile("\\b\\S+\\b");

    public static String pigIt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException();
        }
        StringBuilder sb = new StringBuilder(str);
        Matcher matcher = WORD.matcher(str);
        List<String> words = new ArrayList<>();
        while(matcher.find()) {
            String word = matcher.group();
            words.add(pigay(word));
            str = str.replace(word, "%s");
        }
        return String.format(str, words.toArray());
    }

    protected static String pigay(String s) {
        StringBuilder result = new StringBuilder();
        if (s.length() >= 2) {
            result.append(s, 1, s.length());
            result.append(s.charAt(0));
            result.append("ay");
        }
        return result.toString();
    }

}
