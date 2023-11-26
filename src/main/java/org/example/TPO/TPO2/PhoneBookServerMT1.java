package org.example.TPO.TPO2;

import java.net.*;
import java.io.*;
import java.util.regex.*;

public class PhoneBookServerMT1 extends Thread {

    private PhoneDirectory pd = null;
    private ServerSocket ss = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    private volatile boolean serverRunning = true; // można zakończyć wątek
    // metodą ustalającą
    // wartość tej zmiennej na false

    private String serverTID;              // identyfikator wątku

    public PhoneBookServerMT1(String serverTID, PhoneDirectory pd,
                              ServerSocket ss) {
        this.serverTID = serverTID;
        this.pd = pd;
        this.ss = ss;
        System.out.println("Server " + serverTID + " started");
        System.out.println("listening at port: " + ss.getLocalPort());
        System.out.println("bind address: " + ss.getInetAddress());

        start();    // uruchomienie wątku
    }


    public void run() {
        while (serverRunning) {
            try {
                Socket conn = ss.accept();

                System.out.println("Connection established by " + serverTID);

                serviceRequests(conn);

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }                               // zamknięcie gniazda serwera
        try { ss.close(); } catch (Exception exc) {}
    }

    // Pozoatełe metody m.in.serviceRequest jak w poprzednio pokazanej klaeie

    private void serviceRequests(Socket connection) {
        // ...
    }

    private void writeResp(int rc, String addMsg)
            throws IOException {
        // ...
    }

    public static void main(String[] args) {
        final int SERVERS_NUM = 4;   // liczba serwerów
        PhoneDirectory pd = null;
        ServerSocket ss = null;
        try {
            String phdFileName = args[0];
            String host = args[1];
            int port = Integer.parseInt(args[2]);
            pd = new PhoneDirectory(phdFileName);
            InetSocketAddress isa = new InetSocketAddress(host, port);
            ss =  new ServerSocket();
            ss.bind(isa);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        // Start wielu wątków (serwerow) dzialających równolegle
        // na tym samym gnieżdzie serwera

        for (int i=1; i <= SERVERS_NUM; i++) {
            new PhoneBookServerMT1("serv thread " + i, pd, ss);
        }
    }

}
