package org.example.PPJ.PPJ4;

import javax.swing.*;

public class AnonymousRunnable {

    public AnonymousRunnable() {

        Runnable runner = new Runnable() {
            public void run() {
                int time = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException exc) { return; }
                    System.out.println(time++/60 +  " min. " + time%60 + " sek.");
                }
            }
        };

        new Thread(runner).start();
        String s, out = "";
        while ((s = JOptionPane.showInputDialog("Wprowadź jakiś tekst:")) != null)
            out += " " + s;
        System.out.println(out);
        System.exit(0);
    }

    public static void main(String args[]) {
        new AnonymousRunnable();
    }
}