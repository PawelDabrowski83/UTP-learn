package org.example.PPJ.PPJ1.stringBuffer;

import javax.swing.*;

public class Test {


    public static void main(String args[]) throws InterruptedException {

        int n = Integer.parseInt(JOptionPane.showInputDialog("Liczba operacji"));

        // String
        QTimer t = new QTimer();
        String strA = "";
        for (int i = 1; i <= n; i++)  strA += "A";
        long etA = t.getElapsed();
        System.out.println("String operator +;   Czas: " + etA  + " ms");

        // StringBuffer
        t = new QTimer();
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= n; i++) sb.append("B");
        sb.toString();
        long etB = t.getElapsed();
        System.out.println("StringBuffer append. Czas: " +  etB + " ms");

        System.out.println("Wykonano " + n + " operacji.");
        System.out.println("Relacja String/StringBuffer  = " + (double) etA/etB);
    }
}
