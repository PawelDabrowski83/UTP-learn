package org.example.UTP1.potokiDemo;

import java.io.*;

class PipesShow {

    PipedWriter authorWrites = new PipedWriter(); // potok, do którego pisze autor
    PipedReader duplicatorReads;                  // potok, z ktorego czyta duplikator

    Duplicator dup;

    PipesShow(int numLines, int numWriters) {

        // każdy przepisywacz na swoją przestrzeń pisania
        SpaceToWrite[] writeSpace = new SpaceToWrite[numWriters];
        for (int i=0; i < writeSpace.length; i++)
            writeSpace[i] = new SpaceToWrite(20, 30); // 20 wierszy, 30 kolumn

        try {
            // Połączenie potoku do ktorego pisze autor
            // z nowoutworzonym potokiem, z którego będzie czytał duplikator
            duplicatorReads = new PipedReader(authorWrites);

            // utworzenie duplikatora (on z kolei stworzy i uruchomi przepisywaczy)
            dup = new Duplicator(duplicatorReads, // skąd będzie czytał
                    writeSpace);     // przetstrzeń pisania dla przepisywaczy

            // start wątku duplikatora
            dup.start();

        } catch (IOException exc) {
            System.out.println("Nie można stworzyć duplikatora");
            exc.printStackTrace();
            System.exit(1);
        }

        SpaceToWrite.show(numWriters); // pokazanie ogólnej przestrzeni pisania
        // grupującej przestrzenie pisania
        // każdego przepisywacza

        // Teraz autor będzie pisał!
        // Utworzenie obiektu klasy Autor powoduje rozpoczęcie przez niego pisania

        String words[] = { "Ala", "ma", "kota", "i", "psa" };

        Author autor = new Author(numLines,      // ile wierszy ma napisać
                words,         // z jakich słów składać teksty
                authorWrites); // Dokąd je zapisywać
    }

    public static void main(String args[]) {
        int numLin = 0; // ile wierszy ma napisać autor
        int numWri = 0; // ilu jest przepisywaczy
        try {
            numLin = Integer.parseInt(args[0]);
            numWri = Integer.parseInt(args[1]);
        } catch(Exception exc) {
            System.out.println("Syntax: java  PipesShow numLines numWri");
            System.exit(1);
        }
        new PipesShow(numLin, numWri);
    }
}
