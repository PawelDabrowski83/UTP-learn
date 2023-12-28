package org.example.TPO.TPO2.clientServerPractice.serverClient13;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IterClient extends Thread {
    private String host;
    private int port;
    private int lineCounter;
    private PrintWriter logger;
    private String filename = "IterClient" + getName() + ".txt";
    private String lastReceived;
    private List<String> problems;
    private List<String> answers = new ArrayList<>();

    public IterClient(String host, int port) {
        this.host = host;
        this.port = port;
        problems = prepareProblemsList();
    }

    public static List<String> prepareProblemsList() {
        List<String> numbers = new ArrayList<>();
        int roll = ThreadLocalRandom.current().nextInt(2, 12);
        for (int i = 0; i < roll; i++) {
            int number = ThreadLocalRandom.current().nextInt(-200, 200);
            numbers.add(String.valueOf(number));
        }
        List<String> result = new ArrayList<>();
        result.add("HELLO");
        result.addAll(numbers);
        result.add("STOP");
        return result;
    }

    @Override
    public void run() {
        try (PrintWriter writerToFile = new PrintWriter(new FileWriter(filename))) {
            logger = writerToFile;
            log("Starting client.");

            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            ) {
                socketChannel.socket().setSoTimeout(8000);
                operate(socketChannel);

            } catch (IOException e) {
                e.printStackTrace();
            }


            log("Closing client.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void operate(SocketChannel socketChannel) {
        log("Operating");

        int step = 0;
        while (true) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 2500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String request = "";
        try {
            request = problems.get(step++);
        } catch (IndexOutOfBoundsException e) {
            try {
                socketChannel.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
            log("Sending: " + request);
            sendRequest(socketChannel, request);
            String response = readResponse(socketChannel);
            log("Received: " + response);
            answers.add(response);
            if(response != null && "STOPPING".equals(response.trim())) {
                try {
                    log("Closing channel.");
                    socketChannel.close();
                    break;
                } catch (IOException e) {
                    log("Exception on closing channel.");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendRequest(SocketChannel socketChannel, String message) {
        //message = message + System.lineSeparator();
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        try {
            log("Writing: " + message);
            socketChannel.write(buffer);
        } catch (IOException e) {
            log("Exception on writing.");
            e.printStackTrace();
        }
    }

    private String readResponse(SocketChannel socketChannel) {
        String response = "";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int readBytes = 0;
        try {
            readBytes = socketChannel.read(buffer);
        } catch (IOException e) {
            log("Exception on reading socket.");
            e.printStackTrace();
        }
        if (readBytes == -1) {
            log("Connection closed.");
            try {
                socketChannel.close();
            } catch (IOException e) {
                log("Exception on closing connection.");
                e.printStackTrace();
            }
        }
        if (readBytes > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            response = new String(data);
        }
        return response;
    }



    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCounter++, message)
        );
    }

    public static void main(String[] args) {
//        int repeat = 10;
//        while (repeat-- > 0) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            new IterClient(IterServer.HOST, IterServer.PORT).start();
//        }
    }
}
