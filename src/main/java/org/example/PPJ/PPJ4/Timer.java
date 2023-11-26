package org.example.PPJ.PPJ4;

import java.util.concurrent.ThreadLocalRandom;

public class Timer extends Thread {

    public void run() {
        int time = 0;
        while (!isInterrupted()) {
            try {
                    sleep(1000);
                    checkForBreak();

            } catch (InterruptedException exc) {
                System.out.println("Wątek zliczania czasu zoostał przerwany.");
                return;
            }
            time++;
            int minutes = time / 60;
            int sec = time % 60;
            System.out.println(minutes + ":" + sec);
        }
    }

    private void checkForBreak() {
        int number = ThreadLocalRandom.current().nextInt(1, 21);
        System.out.println(" wylosowana liczba to: " + number);
        if (number == 20) {
            this.getThreadGroup().interrupt();
        }
    }
}
