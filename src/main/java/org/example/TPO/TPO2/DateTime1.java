package org.example.TPO.TPO2;

import java.net.*;
import java.io.*;

class DateTime1 {

    public static void main(String[] args) {

        String host = null;
        int port = 13;

        try {
            host = args[0];  // host - jako argument wywołania

            // Utworzenie gniazda
            Socket socket = new Socket(host, port);

            // Pobranie strumienia wejściowego gniazda
            // Nakładamy dekodowanie i buforowanie
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            // Odczyt odpowiedzi serwera (data i czas)
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            // Zamknięcie strumienia i gniazda
            br.close();
            socket.close();
        } catch (UnknownHostException exc) {
            System.out.println("Nieznany host: " + host);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
