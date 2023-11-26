package org.example.PPJ.PPJ4;

import javax.swing.*;

public class Quiz1a extends Thread {

    private final String[] CAP = {"Praga", "Bratysława", "Moskwa",
            "Berlin", "Kijów", "Wilno", "Mińsk" };

    private boolean[] entered = new boolean[CAP.length];

    private int time = 0; // licznik czasu

    public Quiz1a() {
        int n = CAP.length;

        JOptionPane.showMessageDialog(null, "Podaj stolice sąsiadujących krajów");
        String askMsg = "Wpisz kolejną stolicę:" ;
        int count = 0;

        // Uruchomienie wątku zliczania czasu
        start();

        while (count < CAP.length) {
            String input = JOptionPane.showInputDialog("Odpowiedzi: " + count + '/' + n +
                    ".   Czas: " + getTime() + '\n' +
                    askMsg);
            if (input == null) break;
            if (isOk(input)) count++;
        }
        JOptionPane.showMessageDialog(null, "Czas wpisywania: " + getTime());
        System.exit(0);
    }

    // Kod który wykonuje się w odrębnym wątku
    public void run() {
        while (true) {
            try {
                this.sleep(1000);
            } catch(InterruptedException exc) {
                System.out.println("Wątek zliczania czasu został przerwany.");
                return;
            }
            time++;
            System.out.println(getTime());
        }
    }

    // Metoda zwracająca bieżący czas w formie min : sek
    private String getTime() {
        int minutes = time/60;
        int sec = time%60;
        return minutes + ":" + sec;
    }

    private boolean isOk(String s) {
        for (int i=0; i < CAP.length; i++) {
            if (s.equalsIgnoreCase(CAP[i]) && !entered[i])
                return (entered[i] = true);
        }
        return false;
    }


    public static void main(String args[]) {
        new Quiz1a();
    }
}
