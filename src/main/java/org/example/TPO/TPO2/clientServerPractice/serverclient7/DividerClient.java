package org.example.TPO.TPO2.clientServerPractice.serverclient7;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

public class DividerClient extends Thread {
    private int counter;
    private final String host;
    private final int port;
    public final String name;
    public DividerClient(String name, String host, int port, int counter) {
        this.name = name;
        this.counter = counter;
        this.host = host;
        this.port = port;
        start();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writerToFile = new PrintWriter(Files.newOutputStream(Paths.get("divisionLog.txt")), true)
        ) {
            while (counter > 1 && !isInterrupted()) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(0, 1500));
                sendRequest(writer, writerToFile, String.valueOf(counter));
                String response = reader.readLine();
                counter = Integer.parseInt(response);
            }
            sendRequest(writer, writerToFile, "END");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(PrintWriter writer, PrintWriter writerToFile, String message) {
        writer.println(message);
        writerToFile.println(message);
    }

    public static void main(String[] args) throws InterruptedException {
                new DividerClient("T1", DividerServer.HOST, DividerServer.PORT, 32);
                new DividerClient("T2", DividerServer.HOST, DividerServer.PORT, 7);
                new DividerClient("T3", DividerServer.HOST, DividerServer.PORT, 111);

    }
}
