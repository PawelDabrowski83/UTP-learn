package org.example.TPO.TPO2.clientServerPractice.serverclient1;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Client extends Thread {
    public static final String CLIENT_IP = "localhost";
    public static final int CLIENT_PORT = 60106;
    public static AtomicInteger clientCounter = new AtomicInteger(0);
    private int clientId;
    private int localClientPort;
    private int iter = 0;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public Client() {
        this.clientId = clientCounter.getAndIncrement();
        localClientPort = CLIENT_PORT + clientId;

        try {
            socket = new Socket(Standard.SERVER_IP, Standard.SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            start();
        } catch (IOException e) {
            System.out.println("Niemożliwość przypisania odpowiednich zmiennych");
            e.printStackTrace();
        }
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run() {
        boolean isActive = true;

        try {
            System.out.println("client start");
                String request, response;
                request = Standard.MESSAGE.INIT.getMessage();
                writer.write(request);
                response = reader.readLine();
                if (Standard.MESSAGE.CONFIRM.getMessage().equals(response)) {
                    request = Standard.MESSAGE.HANDSHAKE.getMessage();
                    writer.write(request);


                    response = reader.readLine();
                    while(!Standard.MESSAGE.ERROR.getMessage().equals(response) || iter != 100) {
                        request = "44 Korzystam z zasobów serwera. Iter: " + iter++;
                        writer.write(request);
                        int pause = Standard.getSafeRandom(0, 1000);
                        Thread.sleep(pause);
                    }


                }


        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
