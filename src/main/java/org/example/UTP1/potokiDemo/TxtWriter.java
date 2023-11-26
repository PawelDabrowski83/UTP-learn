package org.example.UTP1.potokiDemo;

import java.io.*;

public class TxtWriter extends Thread {  // Klasa przepisywacza

    private LineNumberReader in;   // strumień skąd czyta
    private SpaceToWrite spw;      // miejsce gdzie pisze

    public TxtWriter(String name,      // nazwa przepisywacza
                     Reader in_,       // z jakiego strumeinia czyta
                     SpaceToWrite spw_ // gdzie pisze
    )
    {
        super(name);
        in = new LineNumberReader(in_);  // filtrowanie strumienia
        // by mieć numery wierszy
        spw = spw_;
    }

    // Kod wątku przepisywacza
    // czyta wiersze ze strumienia wejściowego
    // i zapisuje je w miejscu oznaczanym spw (SpaceToWrite)
    // dopóki nie nadszedl sygnał o końcu pracy (tekst "Koniec pracy")
    public void run() {
        spw.writeLine(" *** " + getName() + " rozpoczął pracę" + " ***");
        spw.writeLine("---> czekam na teksty !");
        String txt;
        try {
            txt = in.readLine();
            while(!txt.equals("Koniec pracy")) {
                spw.writeLine(in.getLineNumber() + " " + txt);
                txt = in.readLine();
            }
            in.close();
            spw.writeLine("**** " + getName() + " skończył pracę");
        } catch(IOException exc) {
            spw.writeLine("****" + getName() + " - zakonczenie na skutek bledu");
            exc.printStackTrace();
            return;
        }
    }
}