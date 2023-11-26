package org.example.TPO.TPO2;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;

public class SimpleGet {

    public SimpleGet(String urlString) {
        try {
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream() // zwraca InputStream
                            // związany z URLem
                    ));
            // Częsci URL-a
            String protocol = url.getProtocol();
            String host = url.getHost();
            String file = url.getFile();
            System.out.println("Protocol: " + protocol);
            System.out.println("Host: " + host);
            System.out.println("File: " + file);

            // Zapiszemy dokument za pomocą strumienia StringWriter
            StringWriter sw = new StringWriter(10240);
            String line;
            while ((line = in.readLine()) != null) {
                sw.write(line);
            }
            in.close();

            // Matcher do wyodrebniania referencji do plików graficznych na stronie
            Matcher matcher = Pattern.compile("img src=\"(.+?)\"",
                    Pattern.CASE_INSENSITIVE).matcher(sw.toString());

            // Obrazy będziemy przechosywać na liście
            java.util.List imgList = new ArrayList();

            while (matcher.find()) {
                String imgRef = matcher.group(1); // wyodrębniamy zapisany na stronie
                // url obrazka
                // zwykle będzie relatywny
                // za pomocą konstruktora URL(kontekst, referencja)
                // uzyskamy właściwy lokalizator wskazujący zasób
                URL imgUrl = new URL(url, imgRef);
                System.out.println(imgRef + " ==> " + imgUrl);
                // utworzenie obiektu Image i dodanie do listy
                imgList.add(Toolkit.getDefaultToolkit().createImage(imgUrl));
            }
            System.out.println("Preparation complete... wait for viewer init");

            // Listę obrazów przekażemy utworzonemu obiektowi przeglądarki obrazków
            new ImageViewer(imgList);

        } catch (MalformedURLException exc) {
            exc.printStackTrace();
            System.exit(1);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String url = JOptionPane.showInputDialog("Adres");
        new SimpleGet(url);
    }
}
