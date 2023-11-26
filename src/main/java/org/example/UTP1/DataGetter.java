package org.example.UTP1;

import java.io.IOException;
import java.io.InputStream;

class DataGetter extends Thread {

    InputStream in;

    public DataGetter(InputStream i) {
        in = i;
    }

    public void run() {
        try {
            int c;
            while ((c = in.read()) != -1) System.out.println((char) c);
        } catch(IOException exc) { return; }
    }
}
