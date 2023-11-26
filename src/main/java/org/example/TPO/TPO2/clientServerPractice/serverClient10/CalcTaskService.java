package org.example.TPO.TPO2.clientServerPractice.serverClient10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcTaskService implements Problemable {
    private static final String FILENAME = "MathProblems.txt";
    public static final Pattern VALID_MATH_PROBLEM = Pattern.compile("(-?\\d+)\\s*([+-/*])\\s*(-?\\d+)");
    private final List<String> validProblems = new ArrayList<>();

    public CalcTaskService() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line = reader.readLine();
            while (line != null) {
                Matcher matcher = VALID_MATH_PROBLEM.matcher(line);
                if (matcher.find()) {
                    String firstNum = matcher.group(1);
                    String operator = matcher.group(2);
                    String secondNum = matcher.group(3);
                    String result = String.join(";", firstNum, operator, secondNum);
                    validProblems.add(result);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMathProblem() throws IllegalStateException {
        if (!validProblems.isEmpty()) {
            int limit = validProblems.size();
            int random = ThreadLocalRandom.current().nextInt(limit);
            return validProblems.get(random);
        }
        throw new IllegalStateException("There are no Math Problems to calculate");
    }

    @Override
    public String getProblem() throws IllegalStateException {
        return getMathProblem();
    }

    public static void main(String[] args) {
        new CalcTaskService();
    }
}
