package org.example.TPO.TPO2.clientServerPractice.serverclient6;

import java.io.*;
import java.net.Socket;

public class CountingClient {
    private int counter;
    public CountingClient(String host, int port, int number) {
        try (Socket socket = new Socket(host, port);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        PrintWriter writerToFile = new PrintWriter(new File("CountingLog.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            counter = number;
            sendRequest(writer, writerToFile, String.valueOf(counter));
            String response = "";
            while (true) {
                response = reader.readLine();
                System.out.println("Read: " + response);
                counter = Integer.parseInt(response);
                if (counter > 0) {
                    sendRequest(writer, writerToFile, String.valueOf(counter));
                    System.out.println("Sent: " + counter);
                } else {
                    break;
                }
            }
            sendRequest(writer, writerToFile, "CLOSED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRequest(PrintWriter writer, PrintWriter writerToFile, String message) {
        writer.println(message);
        writerToFile.println(message);
        System.out.println("Written: " + message);
    }

    public static void main(String[] args) {
        new CountingClient(CountingServer.HOST, CountingServer.PORT, 47);
    }
}
