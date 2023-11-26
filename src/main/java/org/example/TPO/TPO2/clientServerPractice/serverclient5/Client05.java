package org.example.TPO.TPO2.clientServerPractice.serverclient5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client05 {
    public Client05(String host, int port) {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Client connected.");
            sendRequest(writer, "Echo");
            System.out.println("Echo sent");
            System.out.println(reader.readLine());
            sendRequest(writer, "Lorem Ipsum");
            System.out.println(reader.readLine());
            sendRequest(writer, "12344");
            System.out.println(reader.readLine());
            sendRequest(writer, "END");
            System.out.println(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client disconnected");
    }

    private void sendRequest(PrintWriter writer, String message) {
        writer.write(message + System.lineSeparator());
    }

    private String readResponse(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    public static void main(String[] args) {
        new Client05(Server05.HOST, Server05.PORT);
    }
}
