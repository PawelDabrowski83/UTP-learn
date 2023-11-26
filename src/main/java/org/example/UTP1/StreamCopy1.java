package org.example.UTP1;

import java.io.*;

public class StreamCopy1 {
    static public void main(String[] args)  {
        try {
            // System.out.println(System.getProperty("user.dir"));
            FileReader in1 = new FileReader("plik0.txt");
            FileWriter out1 = new FileWriter("plik1.txt");
            Stream.copy(in1, out1);
            in1.close();
            out1.close();

            String msg = "Ala ma kota";
            StringReader in2 = new StringReader(msg);
            FileWriter out2 = new FileWriter("plik2.txt");
            Stream.copy(in2, out2);
            in2.close();
            out2.close();
        } catch(IOException exc) {
            exc.printStackTrace();
        }

    }
}
class Stream {

    static void copy(InputStream in, OutputStream  out) throws IOException {
        int c;
        while ((c = in.read()) != -1) out.write(c);
    }

    static void copy(Reader in, Writer out) throws IOException {
        int c;
        while ((c = in.read()) != -1) out.write(c);
    }
}

class Lines {

    public static void main(String args[]) {
        try {
            FileReader fr = new FileReader("plik0.txt");
            LineNumberReader lr = new LineNumberReader(fr);
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter("plik1.txt"));

            String line;
            while  ((line = lr.readLine()) != null) {
                bw.write( lr.getLineNumber() + " " + line);
                bw.newLine();
            }
            lr.close();
            bw.close();
        } catch(IOException exc) {
            System.out.println(exc.toString());
            System.exit(1);
        }

    }
}