package org.example.TPO.TPO1;

import java.nio.*;

public class Endianness {

    static void show(int n) {
        String s = Integer.toHexString(n);
        int l = s.length();
        for (int i=l; i < 8; i++) s = '0' + s;
        System.out.println("Liczba " + n + " hex -> " + s.toUpperCase());
    }

    public static void main(String args[]) {
        int num = 1234;
        ByteBuffer buf = ByteBuffer.allocate(4);
        System.out.println(buf.order().toString());
        IntBuffer b1 = buf.asIntBuffer();
        System.out.println("Porządek b1 " + b1.order());
        b1.put(num);
        b1.flip();
        show(b1.get());
        buf.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println("Porządek buf " + buf.order());
        System.out.println("Porządek b1 " + b1.order());
        b1.rewind();
        show(b1.get());
        System.out.println("Porządek buf " + buf.order());
        System.out.println("Porządek dziedziczony " + buf.asIntBuffer().order());
        show(buf.asIntBuffer().get());
    }
}