package org.example.TPO.TPO1;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.channels.FileChannel;

public class BuffChan {

    String fname = "testfile.tmp";

    // inicjacja danych testowych
    void init() throws Exception {
        double[] data = { 0.1, 0.2, 0.3 };
        DataOutputStream out = new DataOutputStream(
                new FileOutputStream(fname)
        );
        for (int i=0; i < data.length; i++) out.writeDouble(data[i]);
        out.close();

    }

    BuffChan() throws Exception {

        // inicjalcja danych testowych
        init();

        // utworzenie bufora
        ByteBuffer buf = ByteBuffer.allocate(1000); // nie wiemy ile, maks 100B

        // uzyskanie kanału
        FileChannel fcin = new FileInputStream(fname).getChannel();

        // czytanie z kanału do bufora
        fcin.read(buf);
        fcin.close();

        // przestawienie bufora bajtowego
        buf.flip();

        // Utworzenie widoku bufora
        DoubleBuffer dbuf = buf.asDoubleBuffer();

        // Wypisanie odczytanych danych
        while (dbuf.hasRemaining()) System.out.println(dbuf.get());
    }

    public static void main(String args[]) throws Exception {
        new BuffChan();
    }
}
