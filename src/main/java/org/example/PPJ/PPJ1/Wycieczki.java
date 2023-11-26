package org.example.PPJ.PPJ1;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class Wycieczki {

    public static void main(String[] args) {

        String[] dest  = { "Bali", "Cypr", "Ibiza", "Kenia", "Kuba" };
        double[] price = { 5000, 2500, 2800, 4500, 6000 };

        String msg = "Wybierz kierunek - " +
                " wpisując literę A-"+ (char) ('A'+dest.length-1)+ ":\n";

        for (int i=0; i < dest.length; i++)
            msg += (char) ('A' + i) + " - " + dest[i] + '\n';

        String res;
        while ((res = showInputDialog(msg)) != null) {
            if (res.length() == 1) {
                int i = res.toUpperCase().charAt(0) - 'A';
                if (i < 0 || i > dest.length -1) continue;
                showMessageDialog(null, dest[i] + " - cena: " + price[i]);
            }
        }
    }

}
