package org.example.TPO.TPO2;

import java.net.*;
import java.io.*;
import java.lang.reflect.*;

class DateTime2 {

    public static void main(String[] args) {

        final String HOST = "time.nist.gov";
        final int PORT = 13;
        Socket socket = new Socket();

        try {
            // Utworzenie adresów
            InetAddress inetadr = InetAddress.getByName(HOST);
            InetSocketAddress conadr = new InetSocketAddress(inetadr, PORT);

            // Połaczenie z serwerem
            // Określenie maksymalnego czasu oczekiwania na połączenie
            socket.connect(conadr, 200);


            // Pobranie strumienia wejściowego gniazda
            // Nakładamy buforowanie
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            // Okreslenie maksymalnego czasu oczekiwania na odczyt danych z serwera
            socket.setSoTimeout(200);

            // Czego możemy się dowiedzieć o stanie gniazda?
            report(socket);

            // Odczyt odpowiedzi serwera (data i czas)
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            // Zamknięcie strumienia i gniazda
            br.close();
            socket.close();
        } catch (UnknownHostException exc) {
            System.out.println("Nieznany host: " + HOST);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    // Dynamiczne wołanie metod z klasy Socket
    static void report(Socket s) throws Exception {
        Method[] methods = (java.net.Socket.class).getMethods();
        Object[] args = {};
        for (int i=0; i<methods.length; i++) {
            String name = methods[i].getName();
            if ((name.startsWith("get") || name.startsWith("is")) &&
                    !name.equals("getChannel") &&
                    !name.equals("getInputStream") &&
                    !name.equals("getOutputStream")) {

                System.out.println(name + "() = " +
                        methods[i].invoke(s, args));
            }
        }
    }


}
