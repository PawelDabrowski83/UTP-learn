package org.example.TPO.TPO2.clientServerPractice.serverclient7;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class DividerServer {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private int lineCounter = 0;
    private volatile boolean serverRunning = true;

    public DividerServer() {

        try (ServerSocket serverSocket = new ServerSocket()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
            serverSocket.bind(inetSocketAddress);
            while (serverRunning) {


                try (Socket socket = serverSocket.accept();
                     PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                     PrintWriter writerToFile = new PrintWriter(new FileWriter("DivisionServerLog.txt", true), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    while (true) {
                        String request = reader.readLine();
                        if (!"END".equals(request)) {
                            AtomicInteger result = new AtomicInteger(0);
                            try {
                                result = new AtomicInteger(Integer.parseInt(request));
                                result = calculateResult(result);
                                sendRequest(writer, writerToFile, String.valueOf(result));
                            } catch (NumberFormatException e) {
                                break;
                            }

                        }
                        System.out.println("Loop " + lineCounter);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AtomicInteger calculateResult(AtomicInteger number) {
        return new AtomicInteger(number.get() / 2);
    }

    private void sendRequest(PrintWriter writer, PrintWriter writerToFile, String message) {
        writer.println(message);
        writerToFile.printf("%d || %s" + System.lineSeparator(), lineCounter++, message);
    }

    public static void main(String[] args) {
        new DividerServer();
    }
}
