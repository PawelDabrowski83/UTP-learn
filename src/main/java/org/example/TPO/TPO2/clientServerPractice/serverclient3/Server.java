package org.example.TPO.TPO2.clientServerPractice.serverclient3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    public Server() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
            serverSocket.bind(inetSocketAddress);

            try (Socket socket = serverSocket.accept();
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                System.out.println("Connection open");
                boolean isActive = false;
                while (true) {
                    System.out.println("in while");
                    String response = "";
                    if (!isActive) {
                        String request = reader.readLine();
                        System.out.println("Message received: " + request);
                        if ("HELLO".equals(request)) {
                            response = "HI";
                            isActive = true;
                        } else {
                            int counter = Integer.parseInt(request);
                            response = String.valueOf(calculateValue(counter));
                            isActive = true;
                        }
                    }
                    if (isActive) {
                        writer.write(response + System.lineSeparator());
                        isActive = false;
                    }

                    isActive = true;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calculateValue(int i) {
        return i--;
    }

    public static void main(String[] args) {
        new Server();
    }
}
