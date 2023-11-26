package org.example.TPO.TPO2.clientServerPractice.serverClient8;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CapitaliserServer {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private volatile boolean isServerRunning = true;
    private int lineCounter = 0;
    private String threadName = "";

    public CapitaliserServer() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
            serverSocket.bind(inetSocketAddress);
            while (isServerRunning) {
                try (Socket socket = serverSocket.accept();
                     PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                     PrintWriter writerToFile = new PrintWriter(new FileWriter("CapitaliserServerLog.txt", true), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    connectionLoop:
                    while (true) {
                        String request = reader.readLine();
                        switch(request) {
                            case "HELLO":
                                sendResponse(writer, writerToFile, "HELLO");
                                threadName = reader.readLine();
                                sendResponse(writer, writerToFile, "CONFIRM");
                                break;
                            case "FINISHED":
                                logToFile(writerToFile, "FINISHED");
                                break connectionLoop;
                            default:
                                String response = process(request);
                                sendResponse(writer, writerToFile, response);
                        }
                    }
                    threadName = "";

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendResponse(PrintWriter writer, PrintWriter writerToFile, String message) {
        writer.println(message);
        logToFile(writerToFile, message);
    }

    private void logToFile(PrintWriter writerToFile, String message) {
        writerToFile.printf("%d || %s || %s" + System.lineSeparator(), lineCounter++, threadName, message);
    }

    private String process(String request) {
        return request.toUpperCase();
    }

    public static void main(String[] args) {
        new CapitaliserServer();
    }
}
