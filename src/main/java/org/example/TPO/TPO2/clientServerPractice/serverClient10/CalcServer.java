package org.example.TPO.TPO2.clientServerPractice.serverClient10;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class CalcServer {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private int lineCounter = 0;
    private final String filename = "CalcServerLog.txt";

    private final Solvable solver;

    public CalcServer(Solvable solver) {
        this.solver = solver;
        operate();
    }

    private void operate() {
        try (PrintWriter logger = new PrintWriter(new FileWriter(filename, true), true)) {
            log(logger, "Server is now operating.");

            try (ServerSocket serverSocket = new ServerSocket()) {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
                serverSocket.bind(inetSocketAddress);
                serverSocket.setSoTimeout(10000);
                log(logger, "Server socket opened.");

                lookingForConnections:
                while(true) {
                    try (Socket socket = serverSocket.accept();
                         PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                         ) {
                        log(logger, "Connection opened.");
                        String request = reader.readLine();
                        log(logger, String.format("Received: %s", request));
                        String response = solver.solve(request);
                        sendResponse(writer, logger, response);
                        log(logger, "Connection closed.");
                    }
                }
            } catch (SocketTimeoutException e) {
                log(logger, "Connection time out.");
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(PrintWriter logger, String message) {
        logger.println(
                String.format("%d | %s", lineCounter++, message)
        );
    }

    private void sendResponse(PrintWriter writer, PrintWriter logger, String message) {
        writer.println(message);
        log(logger, message);
    }

    public static void main(String[] args) {
        new CalcServer(new CalcTaskSolver());
    }
}
