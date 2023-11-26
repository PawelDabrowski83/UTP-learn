package org.example.TPO.TPO2.clientServerPractice.serverClient10;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class CalcClient extends Thread {
    private Problemable sourceOfSolvableProblems;
    private String host;
    private int port;
    private AtomicInteger lineCounter = new AtomicInteger(0);
    public CalcClient(String host, int port, Problemable sourceOfSolvableProblems) {
        this.sourceOfSolvableProblems = sourceOfSolvableProblems;
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
                int repeats = 3;
                while (repeats-- > 0) {
                    String problem = sourceOfSolvableProblems.getProblem();
                    logToFile(logger, String.format("My problem is: %s", problem));
                    sendRequest(writer, logger, problem);
                    String response = reader.readLine();
                    logToFile(logger, String.format("Received: %s", response));
                }
                logToFile(logger, "Calculations over.");



            } catch (ConnectException e) {
                logToFile(logger, "Connection refused.");
            }


            logToFile(logger, "Client ending.");
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

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(500);
                new CalcClient(CalcServer.HOST, CalcServer.PORT, new CalcTaskService()).start();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
