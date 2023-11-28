package org.example.TPO.TPO2.clientServerPractice.serverClient12;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class RelayClient extends Thread {
    private int lineCouner = 0;
    private Selector selector;
    private SocketChannel socketChannel;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private PrintWriter logger;
    private final String FILENAME = RelayClient.class.getSimpleName() + getName() + "Log.txt";
    private String host;
    private int port;

    public RelayClient(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.logger = new PrintWriter(new FileWriter(FILENAME, true), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        log("Test");
        System.out.println(logger.toString());
        closeAllFinally();
    }

    private void closeAllFinally() {
        logger.close();
        printWriter.close();
        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketChannel.close();
        bufferedReader.close();
    }

    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCouner++, message)
        );
    }

    public static void main(String[] args) {
        new RelayClient(RelayServer.HOST, RelayServer.PORT).start();
    }
}
