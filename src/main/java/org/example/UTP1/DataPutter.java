package org.example.UTP1;

import java.io.IOException;
import java.io.OutputStream;

public class DataPutter extends Thread {

    OutputStream out;

    public DataPutter(OutputStream o) {
        out = o;
    }

    public void run() {
        try {
            for (char c = 'a'; c <= 'z'; c++) out.write(c);
            out.close();
        } catch(IOException exc) { return; }

    }
}