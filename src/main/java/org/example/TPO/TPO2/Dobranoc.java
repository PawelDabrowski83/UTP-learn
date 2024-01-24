package org.example.TPO.TPO2;

public class Dobranoc {
    public static void main(String[] args) {
        Thread localThread = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                System.out.println("Test!");

            }
        });
        localThread.start();
    }
}
