package org.example.PPJ.PPJ1;

import javax.swing.*;

public class JavaWelcome {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Powitanie");                              // 1
        String htmlText = "<html><FONT SIZE=+3>" +                           // 2
                "Witaj<font color=red><b> Javo!</b></font><br>" +
                "<font color=blue>... A witaj!</font></html>";

        Icon icon = new ImageIcon("java_logo.png");                          // 3
        JLabel label = new JLabel(htmlText, icon, JLabel.CENTER);            // 4
        frame.add(label);                                                    // 5
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                // 6
        frame.pack();                                                        // 7
        frame.setLocationRelativeTo(null);                                   // 8
        frame.setVisible(true);                                              // 9

    }

}
