package org.example.TPO.TPO2.clientServerPractice.serverclient3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private int counter;
    public Client(String host, int port, int counter) {
        this.counter = counter;
        try (Socket socket = new Socket(host, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            operateRequest(socket, writer, reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void operateRequest(Socket socket, PrintWriter writer, BufferedReader reader) {
        print("Connection started.");
        writer.write("HELLO" + System.lineSeparator());
        print("Written hello");
        try {
            String response = reader.readLine();
            if (!"HI".equals(response)) {
                print("Error. Closing connection");
                socket.close();
            }
            while (counter > 0) {
                writer.write(counter-- + System.lineSeparator());
                response = reader.readLine();
                counter = Integer.parseInt(response);
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void print(String message) {
        System.out.println("c| " + message);
    }

    public static void main(String[] args) {
        new Client(Server.HOST, Server.PORT, 18);
    }
}
