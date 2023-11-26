package org.example.TPO.TPO1;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.example.TPO.TPO1.Buffers.showParms;

public class BufView1Alt {
    public static void main(String args[]) {

        final int SHORT_SIZE = 2;
        ByteBuffer bb = ByteBuffer.allocate(10*SHORT_SIZE);
        ShortBuffer sb = bb.asShortBuffer();
        short a = 1, b = 2, c = 3;
        sb.put(a).put(b).put(c);
        showParms("bufor short - po dodaniu liczb", sb);
        showParms("bufor bajtowy - po dodaniu liczb", bb);

        sb.flip();
        showParms("bufor short - po przestawieniu", sb);
        showParms("bufor bajtowy - po flip bufora short", bb);
        System.out.print("Dane");
        while (bb.hasRemaining()) System.out.print(" " + bb.get());
    }

}
