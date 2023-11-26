package org.example.TPO.TPO2.clientServerPractice.serverClient10;

public class Calculator {
    public static Double calculate(Double firstNum, String operator, Double secondNum) {
        switch(operator) {
            case "-" : {
                return firstNum - secondNum;
            }
            case "*" : {
                return firstNum * secondNum;
            }
            case "+" : {
                return firstNum + secondNum;
            }
            case "/" : {
                return firstNum / secondNum;
            }
        }
        return null;
    }
}
