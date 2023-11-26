package org.example.UTP1.strumienie;

import java.io.*;

public class Stream {

    static void copy(InputStream in, OutputStream  out) throws IOException {
        int c;
        while ((c = in.read()) != -1) out.write(c);
    }

    static void copy(Reader in, Writer out) throws IOException {
        int c;
        while ((c = in.read()) != -1) out.write(c);
    }
}

