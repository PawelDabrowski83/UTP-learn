package org.example.TPO.TPO2.clientServerPractice.serverClient14;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ChatServer implements Runnable {

    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private PrintWriter logger;
    private final String filename = getClass().getSimpleName() + "Log.txt";
    private int lineCounter = 0;

    public ChatServer() {

    }

    @Override
    public void run() {
        try (PrintWriter writeToFile = new PrintWriter(new FileWriter(filename), true)) {
            logger = writeToFile;
            runServer();
        } catch (IOException e) {
            System.out.println("Exception on logging.");
        }
    }

    private void runServer() {
        log("Starting server.");
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            this.serverSocketChannel = ssc;
            try (Selector sel = Selector.open()) {
                this.selector = sel;
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                searchForConnections();
            } catch (IOException e) {
                log("Exception on selector " + e.getMessage());
            }
        } catch (IOException e) {
            log("Exception on opening server socket - " + e.getMessage());
        }
    }

    private void searchForConnections() {
        log("Searching for connections");
        while (serverSocketChannel.isOpen() && !Thread.currentThread().isInterrupted()) {
            int readyChannels = 0;
            try {
                readyChannels = selector.select();
            } catch (IOException e) {
                log("Exception on selecting keys " + e.getMessage());
            }
            if (readyChannels == 0) {
                continue;
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey current = iterator.next();
                if (current.isValid()) {
                    if (current.isAcceptable()) {
                        acceptConnection(current);
                        continue;
                    }
                    if (current.isReadable()) {
                        readRequest(current);
                        continue;
                    }
                }
                iterator.remove();
            }
        }
    }

    private void acceptConnection(SelectionKey current) {
        log("Opening connection with client");
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) current.channel();
        try {
            SocketChannel channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            current.cancel();
        } catch (IOException e) {
            log("Exception on creating connection." + e.getMessage());
        }
    }

    private void readRequest(SelectionKey current) {
        log("Reading request.");
        SocketChannel channel = (SocketChannel) current.channel();
        String request = "";
        request = readFromChannel(channel);
        log("Received: " + request);
        String response = request;
        current.attach(response);
        broadcastResponse(current);
        current.interestOps(SelectionKey.OP_READ);
    }

    private String readFromChannel(SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int bytesRead = 0;
        String message = "";

        try {
            bytesRead = channel.read(buffer);
        } catch (IOException e) {
            log("Exception on read " + e.getMessage());
            safeClose(channel);
        }

        if (bytesRead == -1) {
            log("Connection closed.");
            safeClose(channel);
        }

        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            message = new String(data);
        }
        return message;
    }

    private void broadcastResponse(SelectionKey current) {
        String message = (String) current.attachment();
        log("Start broadcasting: " + message);
        for (SelectionKey key : selector.selectedKeys()) {
            log("Looking for clients");
            if (key.isValid() && key.isReadable()) {
                log("Client found and broadcasting");
                SocketChannel channel = (SocketChannel) key.channel();
                try {
                    channel.write(ByteBuffer.wrap(message.getBytes()));
                } catch (IOException e) {
                    log("Exception on broadcast. " + e.getMessage());
                    safeClose(channel);
                }
            }
        }
    }

    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCounter++, message)
        );
    }

    private void safeClose(SocketChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
            log("Exception on closing channel.");
        }
    }

    public static void main(String[] args) {
        Thread chatServer = new Thread(new ChatServer());
        chatServer.start();
    }
}

