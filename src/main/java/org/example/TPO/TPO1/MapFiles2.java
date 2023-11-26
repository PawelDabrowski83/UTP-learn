package org.example.TPO.TPO1;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

class MapFiles2 {

    public static void main(String[] args) throws Exception  {
        Charset inCharset = Charset.forName("windows-1250"),
                outCharset = Charset.forName("ISO-8859-2");

        RandomAccessFile file = new RandomAccessFile(args[0], "rw");
        FileChannel fc = file.getChannel();

        // Mapowanie pliku
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE,
                0, (int) fc.size());

        // Utworzenia bufora znakowego ze zdekodowanymi znakami
        // z bufora bajtowego (mapujące plik). Konwersja: win1250->unicode
        CharBuffer cbuf = inCharset.decode(mbb);

        // Okazuje się, że ten nowo utworzony bufor opakowuje tablicę
        // zatem możemy ją uzyskać i działać na jej elementach
        // to dzialanie oznacza dzialanie na elementach bufora
        char[] chArr = cbuf.array();
        for (int i=0; i < chArr.length; i++)
            chArr[i] = Character.toUpperCase(chArr[i]);

        // Po dekodowaniu bufor bajtowy musi być przewinięty do początku
        // aby koder (zob. dalej) mógł w nim zapisywać kodowane dane
        mbb.rewind();

        // Utworzenie kodera, zamieniającego Unicode na wyjściową stronę kodową
        CharsetEncoder encoder = outCharset.newEncoder();

        // Koder zapisuje istniejący bufor mbb (ten który mapuje plik)
        // ostatni argument - true oznacza zakończenie pracy kodera na tym wywołaniu
        encoder.encode(cbuf, mbb, true);
        fc.close();
    }
}
