package org.example.TPO.TPO2.clientServerPractice.serverClient14;

import java.util.Scanner;

/**
 * It appears that you can only close Scanner once and then cannot reopen.
 */
public class MainMultipleScannerTest {
    public static void main(String[] args) {
        String message1 = readWithScanner();
        String message2 = readWithScanner();
        System.out.println(message1 + " " + message2);
    }

    private static String readWithScanner() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Write some text:");
            return scanner.nextLine();
        }
    }
}
