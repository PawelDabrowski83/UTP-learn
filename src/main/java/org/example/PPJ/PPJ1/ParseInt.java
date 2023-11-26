package org.example.PPJ.PPJ1;

import javax.swing.*;

public class ParseInt {

    public static void main(String[] args) {

        String s1 = JOptionPane.showInputDialog("Podaj pierwszą liczbę");
        if (s1 != null) {
            String s2 = JOptionPane.showInputDialog("Podaj drugą liczbę");
            if (s2 != null) {
                int l1 = Integer.parseInt(s1);
                int l2 = Integer.parseInt(s2);
                JOptionPane.showMessageDialog(null, "Suma: " + (l1 + l2));
            }
        }
    }

}
