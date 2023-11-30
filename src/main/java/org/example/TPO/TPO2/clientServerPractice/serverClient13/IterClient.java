package org.example.TPO.TPO2.clientServerPractice.serverClient13;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class IterClient extends Thread {
    private String host;
    private int port;
    private int lineCounter;
    private PrintWriter logger;
    private String filename = "IterClient" + getName() + ".txt";

    public IterClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (PrintWriter writerToFile = new PrintWriter(new FileWriter(filename))) {
            logger = writerToFile;
            log("Starting client.");

            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, port))) {
                socketChannel.configureBlocking(false);
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
        String message = "HELLO";
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        try {
            socketChannel.write(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCounter++, message)
        );
    }
}
