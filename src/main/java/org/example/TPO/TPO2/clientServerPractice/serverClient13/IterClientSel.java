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
    private final String host;
    private final int port;
    private int lineCounter = 0;
    private final String filename = getClass().getSimpleName() + getName() + "Log.txt";
    private PrintWriter logger;
    private Selector selector;
    private final List<String> problems;
    private final List<String> answers = new ArrayList<>();
    private int stepToWrite = 0;

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
                socketChannel.connect(new InetSocketAddress(host, port));
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
//                current.interestOps(SelectionKey.OP_WRITE);
                socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            } catch (IOException e) {
                log("Exception on finishing connection.");
                e.printStackTrace();
                current.cancel();
            }
        }
    }

    private void readResponse(SelectionKey current) {
        boolean isClosing = false;
        SocketChannel socketChannel = (SocketChannel) current.channel();
        String response = readFrom(socketChannel);
        log("Received: " + response);
        answers.add(response);


        if ("STOPPING".equals(response)) {
            isClosing = true;
        }

        if (isClosing) {
            current.cancel();
            try {
                socketChannel.close();
            } catch (IOException e) {
                log("Exception on closing channel.");
                e.printStackTrace();
            }
        } else {
            current.interestOps(SelectionKey.OP_WRITE);
        }
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
        SocketChannel socketChannel = (SocketChannel) current.channel();

        String request = prepareRequest();
        log("Request prepared: " + request);
        if (request == null) {
            current.cancel();
            try {
                log("Closing channel.");
                socketChannel.close();
                return;
            } catch (IOException e) {
                logException("closing channel");
                e.printStackTrace();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.wrap(request.getBytes());
            try {
                log("Writing: " + request);
                socketChannel.write(buffer);
            } catch (IOException e) {
                logException("writing message");
                e.printStackTrace();
            }
        }
        current.interestOps(SelectionKey.OP_READ);
    }

    private String prepareRequest() {
        String request = "";
        try {
            request = problems.get(stepToWrite++);
        } catch (IndexOutOfBoundsException ignored) {
            request = null;
        }
        return request;
    }

    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCounter++, message)
        );
    }

    private void logException(String message) {
        log("Exception on " + message + ".");
    }


    public static void main(String[] args) {
        int repeat = 5;
        while (repeat-- > 0) {
            new IterClientSel(IterServer.HOST, IterServer.PORT).start();
        }
    }
}
