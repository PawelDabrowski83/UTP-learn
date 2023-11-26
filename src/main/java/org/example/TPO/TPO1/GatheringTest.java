package org.example.TPO.TPO1;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

class GatheringTest {

    public static void main(String[] args) throws Exception {

        // To będą stałe części każdego pliku
        String sHeader = "To jest nagłówek. Może być duży";
        String sFooter = "To jest zakończenie. Może być duże";

        // To będą dane, które się zmieniają od pliku do pliku
        byte[][] dane = { { 1, 2, 3},       // dane 1-go pliku
                { 9, 10, 11, 12 }, // dane 2-go pliku
                { 100, 101}        // dane 3-go pliku
        };

        Charset charset = Charset.forName("windows-1250");
        ByteBuffer header = charset.encode(CharBuffer.wrap(sHeader)),
                footer = charset.encode(CharBuffer.wrap(sFooter));

        // Drugi element tablicy buforów będzie dynamicznie się zmienial
        // na razie = null
        ByteBuffer[] contents = { header, null, footer };
        for (int i = 0; i<dane.length; i++) {
            FileChannel fc = new FileOutputStream("plikGathering"+i + ".txt").getChannel();
            contents[1] = ByteBuffer.wrap(dane[i]);  // podstawienie zmiennych danych
            fc.write(contents);                      // zapis danych ze wszystkich buforów!
            fc.close();
            header.rewind();
            footer.rewind();
        }
    }


}
