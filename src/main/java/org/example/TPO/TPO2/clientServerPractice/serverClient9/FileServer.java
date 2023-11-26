package org.example.TPO.TPO2.clientServerPractice.serverClient9;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    public static final String NAME = "Server";
    private volatile boolean isServerActive = true;
    private int lineCounter = 0;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
            serverSocket.bind(inetSocketAddress);
            while (isServerActive) {
                try (Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                PrintWriter writerToFile = new PrintWriter(
                        new FileWriter("FileServerLog.txt", true),
                        true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    boolean isActiveForReading = true;

                    connectionLoop:
                    while(true) {
                        if (isActiveForReading) {
                            String line = reader.readLine();
                            while(line != null && !"STOP\n".equals(line)) {
                                FileUtils.logToFile(writerToFile, line, lineCounter++, NAME);
                                line = reader.readLine();
                            }
                            isActiveForReading = false;
                        }
                        if (!isActiveForReading) {
                            FileUtils.sendResponse(writer, writerToFile, "STOP", lineCounter++, NAME);
                            socket.close();
                            break;
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new FileServer().start();
    }
}
