package org.example.TPO.TPO2.clientServerPractice.serverclient2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 65432;

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket())
        {
            InetSocketAddress isa = new InetSocketAddress(SERVER_IP, SERVER_PORT);
            serverSocket.bind(isa);
            System.out.println("Open server");

            try (Socket socket = serverSocket.accept();
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                System.out.println("Connection open");
                String request = reader.readLine();
                System.out.println(request);
                while (!"END".equals(request) && request != null) {
                    System.out.println("Zapytanie: " + request);
                    writer.println("CONFIRM");

                    System.out.println("Potwierdzenie wysłane");
                    request = reader.readLine();
                    System.out.println("nowy request: " + request);
                }
                System.out.println("Połączenie zakończone");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server closed");
    }

    public static void main(String[] args) {
        new Server();
    }
}
