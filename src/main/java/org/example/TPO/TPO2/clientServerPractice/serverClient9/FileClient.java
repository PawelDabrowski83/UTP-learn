package org.example.TPO.TPO2.clientServerPractice.serverClient9;

import java.io.*;
import java.net.Socket;

public class FileClient extends Thread {
    private final String name;
    private final String host;
    private final int port;
    private int lineCounter = 0;

    public FileClient(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             PrintWriter writerToFile = new PrintWriter(
                     new FileWriter(
                             new File("FileClient" + name + ".txt"), true),
                     true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            boolean isActiveForSending = true;

            connectionLoop:
            while (true) {
                if (isActiveForSending) {
                    String fileToSend = "FileToSend.txt";
                    try (BufferedReader fileReader = new BufferedReader(new FileReader(fileToSend))) {
                        String line = fileReader.readLine();
                        while (line != null) {
                            sendResponse(writer, writerToFile, line);
                            line = fileReader.readLine();
                        }
                        sendResponse(writer, writerToFile, "STOP");
                        isActiveForSending = false;
                    }
                }
                if (!isActiveForSending) {
                    logToFile(writerToFile, "LISTENING");
                    String line = reader.readLine();
                    while (!"STOP\n".equals(line)) {
                        logToFile(writerToFile, line);
                        line = reader.readLine();
                    }
                    logToFile(writerToFile, "ENDED");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logToFile(PrintWriter writeToFile, String message) {
        FileUtils.logToFile(writeToFile, message, lineCounter++, name);
    }

    private void sendResponse(PrintWriter writer, PrintWriter writerToFile, String message) {
        FileUtils.sendResponse(writer, writerToFile, message, lineCounter++, name);
    }

    public static void main(String[] args) {
        new FileClient("T1", FileServer.HOST, FileServer.PORT).start();

    }
}
