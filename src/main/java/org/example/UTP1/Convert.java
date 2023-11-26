package org.example.UTP1;

import java.io.*;

public class Convert {

    public static void main(String[] args) {

        String infile  = "plik0.txt",     // plik wejściowy
                in_enc  = "UTF-8",     // wejściowa strona kodowa
                outfile = "plik0kod.txt",     // plik wyjściowy
                out_enc = "ISO8859_2";     // wyjściowa strona kodowa

        try {
            FileInputStream fis = new FileInputStream(infile);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis, in_enc));
            FileOutputStream fos = new FileOutputStream(outfile);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos, out_enc));
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

    }
}
