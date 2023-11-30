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
//                    if (current.isWritable()) {
//                        sendResponse(current);
//                    }
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
            socketChannel.register(sel, SelectionKey.OP_READ);

        } catch (IOException e) {
            e.printStackTrace();
            log("Exception on establishing connection.");
        }
    }

    private void readRequest(SelectionKey current) {
        log("Reading...");
        try (SocketChannel socketChannel = (SocketChannel) current.channel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = 0;
            try {
                bytesRead = socketChannel.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                log("Exception on reading from buffer.");
            }

            if (bytesRead == -1) {
                log("Connection closed.");
                current.cancel();
            }
            if (bytesRead > 0) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                String message = new String(data);
                log("Received: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("Exception on retrieving channel.");
        }
    }

    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCounter++, message)
        );
    }

    public static void main(String[] args) {
        new IterServer();
    }
}
