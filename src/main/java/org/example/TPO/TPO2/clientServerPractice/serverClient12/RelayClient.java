package org.example.TPO.TPO2.clientServerPractice.serverClient12;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class RelayClient extends Thread {
    private int lineCouner = 0;
    private Selector selector;
    private BufferedReader bufferedReader;
    private ByteBuffer buffer;
    private PrintWriter printWriter;
    private PrintWriter logger;
    private final String FILENAME = RelayClient.class.getSimpleName() + getName() + "Log.txt";
    private String host;
    private int port;

    public RelayClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (PrintWriter logger = new PrintWriter(new FileWriter(FILENAME, true), true)) {
            this.logger = logger;
            log("Client started running.");

            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, port))) {
                log("Opening connection.");
                socketChannel.configureBlocking(false);
                socketChannel.socket().setSoTimeout(10000);
                selector = Selector.open();
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                operate();
                log("Closing connection.");
            } catch (IOException e) {
                log("Connection closed by exception.");
            }

            log("Closing client.");
        } catch (IOException e) {
            throw new IllegalStateException("Cannot start a logger service.");
        }
    }

    private void operate() {
        while(!isInterrupted()) {
            log("Checking the keys");
            try {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while(iterator.hasNext()) {
                    SelectionKey current = iterator.next();

                    if (current.isConnectable()) {
                        handleConnect(current);
                    } else if (current.isReadable()) {
                        readResponse(current);
                    } else if (current.isWritable()) {
                        sendRequest(current);
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log("Exception on selector level.");
            }
        }
    }

    private void handleConnect(SelectionKey current) throws IOException {
        SocketChannel sc = (SocketChannel) current.channel();
        if (sc.isConnectionPending()) {
            sc.finishConnect();
            log("Connected to server.");
        }
        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private void readResponse(SelectionKey current) throws IOException {
        SocketChannel sc = (SocketChannel) current.channel();
        buffer = ByteBuffer.allocate(1024);
        int bytesRead = sc.read(buffer);

        if (bytesRead == -1) {
            current.cancel();
            log("Connection closed by server.");
            sc.close();
        } else if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            log("Received: " + new String(data));
        }

    }

    private void sendRequest(SelectionKey current) {

    }


    private void log(String message) {
        logger.println(
                String.format("%d || %s", lineCouner++, message)
        );
    }

    public static void main(String[] args) {
        new RelayClient(RelayServer.HOST, RelayServer.PORT).start();
    }
}
