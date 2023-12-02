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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class IterClientSel extends Thread {
    private String host;
    private int port;
    private int lineCounter = 0;
    private String filename = getClass().getSimpleName() + getName() + "Log.txt";
    private PrintWriter logger;
    private Selector selector;
    private List<String> problems;
    private List<String> answers = new ArrayList<>();

    public IterClientSel(String host, int port) {
        this.host = host;
        this.port = port;
        this.problems = IterClient.prepareProblemsList();
    }

    @Override
    public void run() {
        try (PrintWriter writeToFile = new PrintWriter(new FileWriter(filename), true)) {
            logger = writeToFile;
            log("Starting client.");
            try {
                SocketChannel socketChannel = SocketChannel.open();
                selector = Selector.open();

                socketChannel.configureBlocking(false);
                socketChannel.socket().bind(new InetSocketAddress(IterServer.HOST, IterServer.PORT));
                socketChannel.socket().setSoTimeout(5000);
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                operate();
                selector.close();
                socketChannel.close();

            } catch (IOException e) {
                log("Exception on opening or closing channel.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            log("Exception on opening logger.");
            e.printStackTrace();
        }
        super.run();
    }

    private void operate() {
        while(true) {
            int readyChannels;
            try {
                readyChannels = selector.select();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey current = iterator.next();

                if (current.isValid()) {
                    if (current.isConnectable()) {
                        finishConnection(current);
                        continue;
                    }
                    if (current.isReadable()) {
                        readResponse(current);
                        continue;
                    }
                    if (current.isWritable()) {
                        sendRequest(current);
                        continue;
                    }
                }

                iterator.remove();
            }
        }
    }

    private void finishConnection(SelectionKey current) {
        SocketChannel socketChannel = (SocketChannel) current.channel();
        if (socketChannel.isConnectionPending()) {
            try {
                log("Connecting.");
                socketChannel.finishConnect();
                current.interestOps(SelectionKey.OP_WRITE);
            } catch (IOException e) {
                log("Exception on finishing connection.");
                e.printStackTrace();
                current.cancel();
            }
        }
    }

    private void readResponse(SelectionKey current) {
        SocketChannel socketChannel = (SocketChannel) current.channel();
        String response = readFrom(socketChannel);
        log("Received: " + response);
    }

    private String readFrom(SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String response = "";
        int bytesRead = 0;

        try {
            bytesRead = socketChannel.read(buffer);
        } catch (IOException e) {
            log("Exception on reading channel.");
            e.printStackTrace();
        }

        if (bytesRead == -1) {
            log("Connection closed.");
            try {
                socketChannel.close();
            } catch (IOException e) {
                log("Exception on closing channel.");
                e.printStackTrace();
            }
        }

        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            response = new String(data);
        }
        return response;
    }

    private void sendRequest(SelectionKey current) {

    }

    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCounter++, message)
        );
    }
}