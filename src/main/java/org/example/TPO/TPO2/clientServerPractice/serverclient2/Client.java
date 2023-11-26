package org.example.TPO.TPO2.clientServerPractice.serverclient2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public Client(String host, int port) {
        try (Socket socket = new Socket(host, port);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Client - connection open");
            int i = 100;
            while (i-- > 0) {
                System.out.println("client: " + i);
                writer.write(i + System.lineSeparator());
                writer.flush();
                String response = reader.readLine();
                System.out.println("Klient: " + response);
            }
            writer.write("END");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Client(Server.SERVER_IP, Server.SERVER_PORT);
    }



}
