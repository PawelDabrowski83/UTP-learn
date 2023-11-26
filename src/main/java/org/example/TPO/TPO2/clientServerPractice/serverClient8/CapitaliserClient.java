package org.example.TPO.TPO2.clientServerPractice.serverClient8;

import java.io.*;
import java.net.Socket;

public class CapitaliserClient extends Thread {
    private boolean isIntroduced = false;
    private final String name;
    private final String[] data;

    public CapitaliserClient(String name, String[] data) {
        this.name = name;
        this.data = data;
        start();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(CapitaliserServer.HOST, CapitaliserServer.PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            while (true) {
                if (!isIntroduced) {
                    sendRequest(writer, "HELLO");
                    String response = reader.readLine();
                    if ("HELLO".equals(response)) {
                        sendRequest(writer, name);
                        if ("CONFIRM".equals(reader.readLine())) {
                            isIntroduced = true;
                        }
                    } else {
                        break;
                    }
                }
                if (isIntroduced) {
                    for (String s : data) {
                        sendRequest(writer, s);
                    }
                    sendRequest(writer, "FINISHED");
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(PrintWriter writer, String message) {
        writer.println(message);
    }

    public static void main(String[] args) {
        String[] data = {"Ala", "ma", "kota"};
        String[] data2 = "Kiedy ranne wstają zorze, nic mu więcej nie pomoże".split(" ");
        String[] data3 = "Litwo, ojczyzno moja, Ty jesteś jak zdrowie. Ile Cię trzeba cenić".split(" ");
        new CapitaliserClient("T1", data);
        new CapitaliserClient("T2", data2);
        new CapitaliserClient("T3", data3);
    }
}
