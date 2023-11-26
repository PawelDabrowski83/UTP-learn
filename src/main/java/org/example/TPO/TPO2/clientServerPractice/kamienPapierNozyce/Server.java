package org.example.TPO.TPO2.clientServerPractice.kamienPapierNozyce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    public static final String HOST = "localhost";
    public static final int PORT = 65432;

    public Server() {
        try {
            serverSocket = new ServerSocket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
            serverSocket.bind(inetSocketAddress);
            socket = serverSocket.accept();
            System.out.println("Server connection open.");
            handleRequest(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRequest(Socket socket) {
        try {

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Reading first message");
            String request = reader.readLine();
            KPN inRequest = KPN.valueOf(request);
            while(inRequest != KPN.NULL) {
                inRequest = KPN.valueOf(request);
                System.out.println("Line: " + request);
                KPN toResponse = KPN.beats(inRequest);
                String response = toResponse.toString() + System.lineSeparator();
                writer.println(response);
                System.out.println("sent" + response);
                request = reader.readLine();
            }
            System.out.println("Service is closed");



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
