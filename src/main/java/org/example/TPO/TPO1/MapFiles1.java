package org.example.TPO.TPO1;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

class MapFiles1 {

    String fname = "test";

    public MapFiles1() throws Exception {
        init();         // inicjacja pliku testowego
        mapAndChange(); // mapowanie i zmiana danych pliku
        checkResult();  // sprawdzenie wyników
    }

    void init() throws IOException {
        int[] data = { 10, 11, 12, 13 };
        DataOutputStream out = new DataOutputStream(
                new FileOutputStream(fname)
        );
        for (int i=0; i<data.length; i++) out.writeInt(data[i]);
        out.close();
    }

    void mapAndChange() throws IOException {

        // Aby dokonywać zmian musimy przyłączyć kanal
        // do pliku otwartego w trybie "read-write"
        RandomAccessFile file =  new RandomAccessFile(fname, "rw");
        FileChannel channel = file.getChannel();

        // Mapowanie pliku
        MappedByteBuffer buf;
        buf  = channel.map(
                FileChannel.MapMode.READ_WRITE,  // tryb "odczyt-zapis"
                0,  // od początku pliku
                (int)channel.size()  // cały plik
        );

        // Uzyskujemy widok na bufor = zmapowany plik
        IntBuffer ibuf = buf.asIntBuffer();

        // Dla ciekawości: jakie charakterystyki widoku
        System.out.println(ibuf + " --- Direct: " +  ibuf.isDirect());

        int i = 0;
        while (ibuf.hasRemaining()) {
            int num = ibuf.get();       // pobieramy kolejny element
            ibuf.put(i++, num * 10);    // zapisujemy jego wartość*10 na jego pozycji    }
        }

        // Zapewnia, że zmiany na pewno zostaną odzwierciedlone w pliku
        buf.force();

        channel.close();
    }

    void checkResult() throws IOException {
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(fname));
            while(true) System.out.println(in.readInt());
        } catch(EOFException exc) {
            return;
        } finally {
            in.close();
        }
    }

    public static void main(String[] args) throws Exception  {
        new MapFiles1();
    }
}
