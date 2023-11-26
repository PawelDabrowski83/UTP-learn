package org.example.TPO.TPO1;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class Ende1 {

    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println("Syntax: in in_enc out out_enc");
            System.exit(1);
        }

        String infile  = args[0],     // plik wejściowy
                in_enc  = args[1],     // wejściowa strona kodowa
                outfile = args[2],     // plik wyjściowy
                out_enc = args[3];     // wyjściowa strona kodowa

        try {
            FileChannel fcin = new FileInputStream(infile).getChannel();
            FileChannel fcout = new FileOutputStream(outfile).getChannel();
            ByteBuffer buf = ByteBuffer.allocate((int)fcin.size());

            // czytanie z kanału
            fcin.read(buf);

            // przeniesienie zawartości bufora do tablicy bytes
            buf.flip();
            byte[] bytes = new byte[buf.capacity()];
            buf.get(bytes);

            // dekodowanie - za pomocą konstruktora klasy String
            String txt = new String(bytes, in_enc);

            // enkodowanie za pomocą metody getBytes z klasy String
            // utworzenie nowego bufora dla kanału wyjściowego
            // zapis do pliku poprzez kanał
            bytes = txt.getBytes(out_enc);
            buf = ByteBuffer.wrap(bytes);
            fcout.write(buf);

            fcin.close();
            fcout.close();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

    }
}
