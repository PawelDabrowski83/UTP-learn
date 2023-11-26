package org.example.TPO.TPO2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketCon {
    public static void main(String[] args) {

        String host = "time.nist.gov";
        int port = 13;
        Socket socket = new Socket(); // utworzenie niezwiązanego gniazda

        try {
            // Utworzenie adresów
            InetAddress inetadr = InetAddress.getByName(host);
            InetSocketAddress conadr = new InetSocketAddress(inetadr, port);

            // Połaczenie z serwerem
            socket.connect(conadr);

            // Pobranie strumienia wejściowego gniazda
            // Nakładamy buforowanie
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
