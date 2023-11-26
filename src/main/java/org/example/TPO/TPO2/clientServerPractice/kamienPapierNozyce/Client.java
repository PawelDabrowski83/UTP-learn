package org.example.TPO.TPO2.clientServerPractice.kamienPapierNozyce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    Socket socket;
    PrintWriter writer;
    BufferedReader reader;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Client connected");
            KPN[] options = new KPN[]{KPN.KAMIEN, KPN.NOZYCE, KPN.PAPIER, KPN.PAPIER, KPN.PAPIER, KPN.KAMIEN, KPN.NULL};
            for (KPN item : options) {
                sendFigure(item);
                System.out.println("Sent: " + item);
                String response = readFigure();
                System.out.println("Received " + response);
            }
            System.out.println("End of client file");

        } catch (IOException e) {
            e.printStackTrace();



        }
    }

    public void closeAll() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFigure(KPN item) {
        writer.println(item.toString());
        System.out.println("Client sent: " + item);
    }

    public String readFigure() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new Client(Server.HOST, Server.PORT);
    }
}
