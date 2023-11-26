package org.example.TPO.TPO2.clientServerPractice.serverClient10;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcTaskSolver implements Solvable {
    public static final Pattern VALID_PROBLEM = Pattern.compile("^(-?\\d+);([+-/*]);(-?\\d+)$");
    @Override
    public String solve(String problem) throws IllegalArgumentException {
        generateDelay();
        if (!validate(problem)) {
            throw new IllegalArgumentException("Input incorrect.");
        }
        String[] inputs = problem.split(";");
        Double firstNum = Double.parseDouble(inputs[0]);
        String operator = inputs[1];
        Double secondNum = Double.parseDouble(inputs[2]);

        Double result = Calculator.calculate(firstNum, operator, secondNum);
        return String.valueOf(result);
    }

    private void generateDelay() {
        int nap = ThreadLocalRandom.current().nextInt(1000, 3000);
        try {
            Thread.sleep(nap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean validate(String message) {
        if (message == null) {
            return false;
        }
        Matcher matcher = VALID_PROBLEM.matcher(message);
        return matcher.find();
    }
}
