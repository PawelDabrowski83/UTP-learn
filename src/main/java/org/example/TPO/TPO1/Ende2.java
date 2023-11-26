package org.example.TPO.TPO1;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;


public class Ende2 {

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

            // Strony kodowe
            Charset inCharset  = Charset.forName(in_enc),
                    outCharset = Charset.forName(out_enc);

            // dekodowanie bufora bajtowego
            buf.flip();
            CharBuffer cbuf = inCharset.decode(buf);

            // enkodowanie bufora znakowego
            // i zapis do pliku poprzez kanał

            buf = outCharset.encode(cbuf);
            fcout.write(buf);

            fcin.close();
            fcout.close();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
