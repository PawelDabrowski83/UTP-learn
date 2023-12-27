package org.example.TPO.TPO2.clientServerPractice.serverClient14;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ChatClient implements Runnable {
    private String host;
    private String filename = getClass().getSimpleName() + "Log.txt";
    private int port;
    private PrintWriter logger;
    private int lineCounter = 0;
    private Selector selector;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (PrintWriter logToFile = new PrintWriter(new FileWriter(filename), true)) {
            logger = logToFile;
            log("Starting client.");
            prepareSocketChannel();
        } catch (IOException e) {
            System.out.println("Exception on opening logger.");
        }
    }

    private void prepareSocketChannel() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(ChatServer.HOST, ChatServer.PORT));
            try (Selector sel = Selector.open()) {
                selector = sel;
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                operate();
            } catch (IOException e) {
                e.printStackTrace();
                logException("opening selector");
            }


        } catch (IOException e) {
            e.printStackTrace();
            logException("opening socket channel");
        }
    }

    private void operate() {
        while (!Thread.currentThread().isInterrupted()) {
            int readyChannels = 0;
            try {
                readyChannels = selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                logException("key selection");
            }
            if (readyChannels == 0) {
                continue;
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey current = iterator.next();
                iterator.remove();

//                if (current.isValid()) {
//                    if (current.isConnectable()) {
//                        connect(current);
//                    }
//                    if (current.isReadable()) {
//                        readResponse(current);
//                    }
//                    if (current.isWritable()) {
//                        sendRequest(current);
//                    }
//                }
            }
        }
    }

    private void log(String message) {
        if (logger != null) {
            logger.println(
                    String.format("%d || %s", lineCounter++, message)
            );
        }
    }

    private void logException(String message) {
        log(String.format("Exception on %s.", message));
    }
}
