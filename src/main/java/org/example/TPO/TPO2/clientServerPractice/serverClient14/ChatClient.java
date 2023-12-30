package org.example.TPO.TPO2.clientServerPractice.serverClient14;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Locale;
import java.util.Scanner;

public class ChatClient implements Runnable {
    private String host;
    private int port;
    private String filename = getClass().getSimpleName() + "Log.txt";
    private PrintWriter logger;
    private int lineCounter = 0;
    private SocketChannel channel;
    private String username;
    private Scanner scanner;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (PrintWriter logToFile = new PrintWriter(new FileWriter(filename), true)) {
            logger = logToFile;
            log("Starting client.");
            prepareSocketChannel();
        } catch (IOException e) {
            System.out.println("Exception on opening logger.");
        }
    }

    private void prepareSocketChannel() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(ChatServer.HOST, ChatServer.PORT));
            channel = socketChannel;
            operate();
        } catch (IOException e) {
            e.printStackTrace();
            logException("opening socket channel");
        }
    }

    private void operate() {
        establishConnection();
        scanner = new Scanner(System.in);
        username = askForUsername();
        openListeningChannel();
        openUserConsole();
        scanner.close();
    }

    private void establishConnection() {
        if (channel.isConnectionPending()) {
            try {
                log("Connecting...");
                channel.finishConnect();
                log("Connected.");
            } catch (IOException e) {
                logException("on establishing connection");
                e.printStackTrace();
            }
        }
    }

    private String askForUsername() {
        System.out.println("Please enter your username: ");
        return scanner.nextLine();
    }

    private void openListeningChannel() {
        Thread listeningThread = new Thread(() -> {
            while (Thread.currentThread().isInterrupted()) {
                String message = readFrom(channel);
                System.out.println(message);
            }
        });
        log("Opening listening channel.");
        listeningThread.start();
    }

    public static String readFrom(SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String message = "";
        int bytesRead = 0;
        try {
            bytesRead = channel.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bytesRead == -1) {
            try {
                channel.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            message = new String(data);
        }
        return message;
    }

    private void openUserConsole() {
        String message = "";
        while (!Thread.currentThread().isInterrupted() && !"quit".equalsIgnoreCase(message)) {
            System.out.printf("[%s]: ", username);
            message = scanner.nextLine();
            sendRequest(message);
        }

    }

    public void sendRequest(String message) {
        message = String.format("[%s]: %s" + System.lineSeparator(), username, message);
        try {
            channel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            logException("writing request to server");
            e.printStackTrace();
        }
    }

    private void log(String message) {
        if (logger != null) {
            logger.println(
                    String.format("%d || %s", lineCounter++, message)
            );
        }
    }

    private void logException(String message) {
        log(String.format("Exception on %s.", message));
    }

    public static void main(String[] args) {
        Thread chatClient = new Thread(new ChatClient(ChatServer.HOST, ChatServer.PORT));
        chatClient.start();
    }
}
