package org.example.TPO.TPO2.clientServerPractice.serverclient6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CountingServer {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private int counter = 0;

    public CountingServer() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
            serverSocket.bind(inetSocketAddress);

            try (Socket socket = serverSocket.accept();
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                operateServer(writer, reader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void operateServer(PrintWriter writer, BufferedReader reader) throws IOException{
        String request = "";
        while (true) {
            if("-1".equals(request)) {
                return;
            }
            request = reader.readLine();
            System.out.println("Received: " + request);
            counter = Integer.parseInt(request);
            calculateCounter();
            writer.println(counter);
            System.out.println("Sent: " + counter);
        }
    }

    private void calculateCounter() {
        counter--;
    }

    public static void main(String[] args) {
        new CountingServer();
    }
}
