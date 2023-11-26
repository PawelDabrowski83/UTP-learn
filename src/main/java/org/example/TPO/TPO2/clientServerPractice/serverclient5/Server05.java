package org.example.TPO.TPO2.clientServerPractice.serverclient5;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server05 {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;

    public Server05() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
            serverSocket.bind(inetSocketAddress);
            try (Socket socket = serverSocket.accept();
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.println("Connection open.");
                String request = reader.readLine();
                System.out.println("Received: " + request);

                while(!"END".equals(request)) {
                    System.out.println("Input: " + request);
                    String response = request;
                    sendRequest(writer, response);
                    request = reader.readLine();
                    if (request != null) {
                        request = request.replaceAll(System.lineSeparator(), "");
                    }
                }
                System.out.println("Closing connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(PrintWriter writer, String message) {
        writer.write("S> " + message + System.lineSeparator());
    }

    public static void main(String[] args) {
        new Server05();
    }
}
