package org.example.UTP1.potokiDemo;

// Klasa, określająca przestrzenie
// na których piszą przepisywacze
// oraz grupująca te przestrzenie w oknie.
// Każdy przepisywacz wypisuje tekst
// do wielowierszowego pola edycyjnego (TextArea z pakietu AWT)
// do czego służy mu metoda writeLine.
// Wszystkie przestrzenie grupowane są w oknie frame.

import java.awt.*;
import java.awt.event.*;

public class SpaceToWrite extends TextArea {

    private static Frame frame = new Frame("Write space");

    // Konstruktor: tworzy nową przetrzeń pisania dla jednego przepisywacza
    public SpaceToWrite(int rows, int cols) {
        super(rows, cols);  // utworzenie TextArea  - z podaną liczbą wierszy, kolumn
        frame.add(this);    // dodanie TextArea do okna
    }

    // Metoda dopisująca nowy wiersz do textarea
    public void writeLine(String s) {
        this.append(s + '\n');
    }

    // Metoda ustalająca ułożenie pól edycyjnych w oknie
    // rozmiar okna (pack daje rozmiar taki jak akurat potrzreba)
    // i pokazująca okno
    public static void show(int numWriters) {
        frame.setLayout(new GridLayout(0, numWriters));
        frame.pack();
        frame.show();

        // Umożliwienie zakończenia aplikacji poprzez zamknięcie okna
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(1);
            }
        });
    }
}
