package org.example.UTP1;

import java.io.*;

public class Potoki {


    public static void main(String[] args) throws IOException {
        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream pin = new PipedInputStream(pout);
        new DataPutter(pout).start();
        new DataGetter(pin).start();
    }

}
