package org.example.UTP1;

public class DefaultEncoding {
    public static void main(String args[])
    {
        String p = System.getProperty("file.encoding");
        System.out.println(p);
    }

}
