package org.example.TPO.TPO2;

import java.io.*;
import java.net.*;

public class EmailClient {

    Socket smtpSocket = null;
    PrintWriter sockOut = null;
    InputStream sockIn = null;

    public void connect(String server, String myDomain) {
        try {
            smtpSocket = new Socket(server, 25);
            sockOut = new PrintWriter(
                    new OutputStreamWriter(smtpSocket.getOutputStream(), "UTF-8"),
                    true);
            sockIn = smtpSocket.getInputStream();

            // Czy połączenie zostało nawiązane?
            // Musi być kod 220 - wtedy Ok
            // Odczytując odpowiedż serwera,
            // sprawdzamy w metodzie readResponse kod 220

            readResponse(220);

            // Przedstawiamy się serwerowi
            // Jeśli nas zaakceptuje - poda kod 250
            doRequest("HELO " + myDomain, 250);


        } catch (UnknownHostException e) {
            System.err.println("Nieznany host: " + server);
            cleanExit(1);
        } catch (IOException exc) {
            System.err.println(exc);
            cleanExit(2);
        }
    }


    // Posyłanie maila
    public void send(String from, String to, String fname) {
        try {

            // Inicjacja transakcji
            // Kod 250 - jesli OK
            doRequest("MAIL FROM:<" + from + ">", 250);

            // Określenie adresata
            // Kod 250 - jesli OK
            doRequest("RCPT TO:<" + to + ">", 250);

            // Posyłanie danych listu
            // Odpowiedź serwera - 354 = jestem gotowy na przyjęcie danych
            doRequest("DATA", 354);

            // Teraz będziemy zapisywać treść listu
            // bezpośrednio do strumienia wyjściowego gniazda
            // Najpierw jakieś nagłówki
            sockOut.println("From: " + from);
            sockOut.println("To: " + to);

            // Czytanie treści listu z pliku
            // Ponieważ samotna kropka w wierszu kończy dane listu
            // to samotną kropkę w treści zamieniamy na dwie kropki
            BufferedReader br = new BufferedReader(
                    new FileReader(fname));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(".")) line += ".";
                sockOut.println(line);
            }
            // Sekwencja CRLF.CRLF oznacza koniec treści listu
            // Drugie CRLF dodane w metodzie doRequest przez println
            doRequest("\\r\\n.", 250);

        } catch (IOException e) {
            System.err.println(e);
            cleanExit(2);
        }
    }

    // Zamknięcie połączenia
    public void closeConnection() {
        try {
            doRequest("QUIT", 221);
        } catch (Exception exc) {
            System.err.println(exc);
        }
        cleanExit(0);
    }


    private void doRequest(String req, int checkCode)
            throws IOException {
        sockOut.println(req);
        System.out.println("Klient: " + req);
        readResponse(checkCode);
    }

    // Uwaga: nie powinniśmy tu stosować buforowania i metody
    // readLine(), ponieważ nie wiadomo ile wierszy zwrówci serwer
    // a wywołanie readLine jest blokujące
    // Zakłądamy: że każda odpowiedź zmieści się w 10000 bajtów
    private void readResponse(int checkCode) throws IOException {
        byte[] readBytes = new byte[10000];
        int num = sockIn.read(readBytes);
        String resp = new String(readBytes, 0, num);
        System.out.println("Serwer: " + resp);
        if (!resp.startsWith(String.valueOf(checkCode)))
            throw new IOException("Niespodziewany kod wyniku od serwera");
    }

    private void cleanExit(int code) {
        try {
            sockIn.close();
            sockOut.close();
            smtpSocket.close();
        }
        catch (Exception exc) {}
        System.exit(code);
    }

    public static void main(String[] args) {
        String server = "mail.somemailer.net";
        String myDomain = "62.125.12.111";
        String from = "me@somemailer.net";
        String to = "you@anywhere";
        EmailClient email = new EmailClient();
        email.connect(server, myDomain);
        email.send(from, to, "list1");
        email.send(from, to, "list2");
        email.closeConnection();
    }
}
