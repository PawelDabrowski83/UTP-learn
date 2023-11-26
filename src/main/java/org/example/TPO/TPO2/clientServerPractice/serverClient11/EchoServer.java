package org.example.TPO.TPO2.clientServerPractice.serverClient11;

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

public class EchoServer {
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private int lineCounter = 0;
    private PrintWriter logger;
    private ServerSocketChannel ssc;
    private Selector selector;

    public EchoServer() {
        try {
            logger = new PrintWriter(new FileWriter("EchoServerLog.txt", true), true);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot run logger on server.");
        }
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {
            ssc = serverSocketChannel;
            this.selector = selector;
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setSoTimeout(8000);
            serverSocketChannel.socket().bind(new InetSocketAddress(HOST, PORT));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            log("Server started, looking for connections.");
            operate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void operate() throws IOException {
        boolean serverIsRunning = true;
        while(serverIsRunning) {
            int keysNumber = selector.select();
            log("Number of keys: " + keysNumber);
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            log("Keys: " + keys.size());

            keyLoop:
            while (iterator.hasNext()) {
                log("Looking for next key.");
                SelectionKey current = iterator.next();
                iterator.remove();

                if (current.isAcceptable()) {
                    acceptConnection(current);
                    continue keyLoop;
                }
                if (current.isReadable()) {
                    readRequest(current);
                    continue keyLoop;
                }
                if (current.isWritable()) {
                    sendResponse(current);
                    continue keyLoop;
                }

            }
        }
    }

    private void sendResponse(SelectionKey current) {
        log("Sending response.");
        try (SocketChannel socketChannel = (SocketChannel) current.channel()) {
            if (!socketChannel.isOpen()) {
                return;
            }
            String response = (String) current.attachment();
            log("Prepared response is: "+ response);
            ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
            socketChannel.write(buffer);
            log("Written to buffer");
            socketChannel.register(current.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readRequest(SelectionKey current) {
        log("Reading request.");
        try (SocketChannel socketChannel = (SocketChannel) current.channel()) {
            if (!socketChannel.isOpen()) {
                return;
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int bytesRead = socketChannel.read(byteBuffer);

            if (bytesRead == -1) {
                log("Connection closed.");
            } else {
                byteBuffer.flip();
                byte[] data = new byte[byteBuffer.remaining()];
                byteBuffer.get(data);
                String request = new String(data);
                log("Received: " + request);
                socketChannel.register(current.selector(), SelectionKey.OP_WRITE | SelectionKey.OP_READ, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnection(SelectionKey current) throws IOException {
        try {
            SocketChannel socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(current.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message) {
        logger.println(
                String.format("%d | %s", lineCounter++, message)
        );
    }

    public static void main(String[] args) {
        new EchoServer();
    }
}