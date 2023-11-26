package org.example.TPO.TPO2.clientServerPractice.serverClient11;


import java.io.*;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class EchoClient extends Thread {
    private int lineCounter = 0;
    private String host;
    private int port;
    private PrintWriter logger;
    private PrintWriter writerHandler;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.logger = new PrintWriter(new FileWriter(this.getClass().getSimpleName() + getName() + ".txt", true), true);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initiate logger." + getName());
        }
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            writerHandler = writer;
            log("Connection started.");
            int random = ThreadLocalRandom.current().nextInt(1, 10);
            log("Random: " + random);
            for (int i = 0; i < random; i++) {
                delay();
                char c = (char) ('a' + i);
                sendRequest(String.valueOf(c));

                delay();
                readResponse(reader);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(String message) {
        writerHandler.println(message);
        log(message);
    }

    private String readResponse(BufferedReader reader) throws IOException {
        String response = reader.readLine();
        log("Received: " + response);
        return response;
    }

    private void log(String message) {
        logger.println(
                String.format("%d | %s", lineCounter++, message)
        );
    }

    private void delay() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        int repeat = 4;
        while (repeat-- > 0) {
            new EchoClient(EchoServer.HOST, EchoServer.PORT).start();
        }
    }
}
