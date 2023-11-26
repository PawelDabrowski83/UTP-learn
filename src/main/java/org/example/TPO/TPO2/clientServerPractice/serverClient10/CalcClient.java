package org.example.TPO.TPO2.clientServerPractice.serverClient10;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class CalcClient extends Thread {
    private String host;
    private int port;
    private AtomicInteger lineCounter = new AtomicInteger(0);
    public CalcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (PrintWriter logger = new PrintWriter(new FileWriter("CalcClientLog" + getName() + ".txt", true), true)) {
            logToFile(logger, String.format("Client %s starting.", getName()));

            try (Socket socket = new Socket(host, port);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                logToFile(logger, String.format("Client %s - connection started with %s.", getName(), socket.getInetAddress()));
            } catch (ConnectException e) {
                logToFile(logger, "Connection refused.");
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(PrintWriter writer, PrintWriter logger, String message) {
        writer.println(message);
        logToFile(logger, message);
    }

    private void logToFile(PrintWriter logger, String message) {
        logger.println(
                String.format("%d | %s", lineCounter.getAndIncrement(), message)
        );
    }

    public static void main(String[] args) {
        new CalcClient("localhost", 65432).start();
    }
}
