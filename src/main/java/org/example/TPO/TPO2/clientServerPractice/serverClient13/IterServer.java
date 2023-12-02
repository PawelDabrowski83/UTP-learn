package org.example.TPO.TPO2.clientServerPractice.serverClient13;

import org.example.TPO.TPO2.clientServerPractice.serverclient1.Server;

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

/**
 * The idea for project is to have a server which will receive a number and resond with number increased by one.
 * The communication will stop when the client sends message "STOP". First communication from the client is its name.
 * Both client and server are using nonblocking socket channels.
 */
public class IterServer {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private int lineCounter = 0;
    private final String filename = "IterServerLog.txt";
    private PrintWriter logger;
    private ServerSocketChannel ssc;
    private Selector sel;

    public IterServer() {
        try (PrintWriter writeToFile = new PrintWriter(new FileWriter(filename), true)) {
            logger = writeToFile;
            log("Starting server.");
            try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                 Selector selector = Selector.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            sel = selector;
            ssc = serverSocketChannel;
            serviceRequests();


            } catch (IOException e) {
                e.printStackTrace();
                log("Exception on opening connection.");
            }

            log("Closing server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serviceRequests() {
        log("Server operating.");
        while(true) {

            int readyChannels = 0;
            try {
                readyChannels = sel.select();
            } catch (IOException e) {
                e.printStackTrace();
                log("Exception on selecting keys.");
            }
            if (readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> keys = sel.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey current = iterator.next();
                iterator.remove();

                if (current.isValid()) {
                    if (current.isAcceptable()) {
                        acceptConnection(current);
                        continue;
                    }
                    if (current.isReadable()) {
                        readRequest(current);
                        continue;
                    }
                    if (current.isWritable()) {
                        sendResponse(current);
                    }
                }
            }
        }




    }

    private void acceptConnection(SelectionKey current) {
        log("Establishing connection.");
        ServerSocketChannel ssc = (ServerSocketChannel) current.channel();
        try {
            SocketChannel socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(sel, SelectionKey.OP_READ, SelectionKey.OP_WRITE);

        } catch (IOException e) {
            e.printStackTrace();
            log("Exception on establishing connection.");
        }
    }

    private void readRequest(SelectionKey current) {
        log("Reading...");
        SocketChannel socketChannel = (SocketChannel) current.channel();
        String request = readFromChannel(socketChannel);
        log("Received: " + request);
        String response = prepareResponse(request);

        current.attach(response);
        current.interestOps(SelectionKey.OP_WRITE);

    }

    private String prepareResponse(String request) {
        String response = "";
        if (request != null && !request.isEmpty()) {
            request = request.trim();
            if ("HELLO".equals(request)) {
                response = "CONFIRM";
            }
            if ("STOP".equals(request)) {
                response = "STOPPING";
            }
            if (isStringNumbered(request)) {
                response = calculateResponse(request);
            }
        }
        return response;
    }

    private boolean isStringNumbered(String word) {
        try {
            Integer.parseInt(word);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private String calculateResponse(String number) {
        int request = Integer.parseInt(number);
        return String.valueOf(++request);
    }

    private void sendResponse(SelectionKey current) {
        log("Sending response.");
        SocketChannel socketChannel = (SocketChannel) current.channel();
        String response = (String) current.attachment();
        if (response == null) {
            log("Response is null");
        }
        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
        try {
            socketChannel.write(buffer);
            log("Writing: " + response);
        } catch (IOException e) {
            log("Error on write.");
            throw new RuntimeException(e);
        }
        current.interestOps(SelectionKey.OP_READ);
    }

    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCounter++, message)
        );
    }

    private String readFromChannel(SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = 0;
        String message = "";
        try {
            bytesRead = socketChannel.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            log("Exception on reading from buffer.");
        }

        if (bytesRead == -1) {
            log("Connection closed.");
            try {
                socketChannel.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    public static void main(String[] args) {
        new IterServer();
    }
}
