package org.example.PPJ.PPJ1.stringBuffer;

import java.io.*;

public class BuffTextFileCopy {

    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;

        try {
            in = new BufferedReader(new FileReader("plik1.txt"));
            out = new BufferedWriter(new FileWriter("out1"));
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch(IOException exc) {
                exc.printStackTrace();
            }
        }

    }

}
