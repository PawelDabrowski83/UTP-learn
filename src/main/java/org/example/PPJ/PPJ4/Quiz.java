package org.example.PPJ.PPJ4;

import javax.swing.*;

public class Quiz {

    // Stolice do odgadnięcia
    private final String[] CAP = {"Praga", "Bratysława", "Moskwa",
            "Berlin", "Kijów", "Wilno", "Mińsk" };

    // Czy stolica była już podana ?
    private boolean[] entered = new boolean[CAP.length];

    public Quiz() {

        int n = CAP.length;

        JOptionPane.showMessageDialog(null, "Podaj stolice lądowych sąsiadów Polski");
        String askMsg = "Wpisz kolejną stolicę:" ;

        int count = 0;  // ile podano prawidłowych odpowiedzi

        // Uruchomienie wątku zliczającego i pokazującego upływający czas
        Timer tm = new Timer();
        tm.start();

        while (count < CAP.length) { // dopóki nie podano wszystkich stolic
            String input = JOptionPane.showInputDialog("Odpowiedzi: " + count + '/' + n +
                    '\n' + askMsg);
            if (input == null) break;
            if (isOk(input)) count++;  // jeżeli ta odpowiedź prawidłowa
        }
        System.exit(0);
    }

    // Czy odpowiedź jest prawidłowa i czy jej wcześniej nie podano?

    private boolean isOk(String s) {
        for (int i=0; i < CAP.length; i++) {
            if (s.equalsIgnoreCase(CAP[i]) && !entered[i])
                return (entered[i] = true);
        }
        return false;
    }

    public static void main(String args[]) {
        new Quiz();
    }
}
