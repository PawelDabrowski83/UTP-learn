package org.example.TPO.TPO2.clientServerPractice.serverClient12;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RelayServer {
    private int lineCounter = 0;
    private static final String FILENAME = RelayServer.class.getSimpleName() + "Log.txt";
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private Selector selector;
    private PrintWriter logger;
    private final int CONNECTION_TIMEOUT = 8000;

    public RelayServer() {
        try (PrintWriter logger = new PrintWriter(new FileWriter(FILENAME, true), true)) {
            this.logger = logger;
            log("Server started.");

            try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
                selector = Selector.open();
                ssc.configureBlocking(false);
                ssc.socket().setSoTimeout(8000);
                ssc.socket().bind(new InetSocketAddress(HOST, PORT));
                ssc.register(selector, SelectionKey.OP_ACCEPT);
                operate();
            } catch (IOException e) {
                log("Connection closed by exception.");
            }

        } catch (IOException e) {
            throw new IllegalStateException("Cannot start logging service.");
        }
    }

    private void operate() {
        long lastConnectionTime = System.currentTimeMillis();
        while (true) {
            try {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    if (System.currentTimeMillis() - lastConnectionTime > CONNECTION_TIMEOUT) {
                        log("Server connection timeout.");
                        break;
                    }
                    continue;
                }
                lastConnectionTime = System.currentTimeMillis();

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey current = iterator.next();

                    if (current.isAcceptable()) {
                        handleAccept(current);
                    } else if(current.isReadable()) {
                        readRequest(current);
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                log("Key-level exception.");
            }
        }
    }

    private void handleAccept(SelectionKey current) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) current.channel();
        SocketChannel sc = serverSocketChannel.accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
        log("Registering connection with client.");
    }

    private void readRequest(SelectionKey current) throws IOException {
        SocketChannel sc = (SocketChannel) current.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int bytesRead = sc.read(buffer);
        if (bytesRead == -1) {
            log("Connection closed by the client.");
            current.cancel();
            sc.close();
        } else if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String message = new String(data);
            String user = sc.getRemoteAddress().toString();
            String niceMessage = String.format("(from: %s) : %s", user, message);
            log(niceMessage);

            broadcast(niceMessage);
        }
    }

    private void broadcast(String message) throws IOException {
        for (SelectionKey key : Selector.open().keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel sc = (SocketChannel) key.channel();
                sc.write(
                        ByteBuffer.wrap(message.getBytes())
                );
            }
        }
    }
    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCounter++, message)
        );
    }

    public static void main(String[] args) {
        new RelayServer();
    }
}


