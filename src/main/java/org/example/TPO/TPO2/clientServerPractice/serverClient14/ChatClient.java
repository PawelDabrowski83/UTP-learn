package org.example.TPO.TPO2.clientServerPractice.serverClient14;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

public class ChatClient implements Runnable {
    private String host;
    private int port;
    private String filename = getClass().getSimpleName() + "Log.txt";
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

                if (current.isValid()) {
                    if (current.isConnectable()) {
                        connect(current);
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
            }
        }
    }

    public void connect(SelectionKey current) {
        SocketChannel channel = (SocketChannel) current.channel();
        if (channel.isConnectionPending()) {
            try {
                log("Connecting...");
                channel.finishConnect();
                log("Connected.");
                channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            } catch (IOException e) {
                logException("on establishing connection");
                e.printStackTrace();
                current.cancel();
            }
        }
    }

    public void sendRequest(SelectionKey current) {
        SocketChannel channel = (SocketChannel) current.channel();
        Scanner scanner = new Scanner(System.in);
        String message = "";
        String username = null;
        while (!Thread.currentThread().isInterrupted() && !"quit".equals(message.toLowerCase(Locale.ROOT))) {
            if (username == null) {
                System.out.println("Please enter your user name: ");
                username = scanner.nextLine();
            }
            System.out.printf("[%s]: ", username);
            message = scanner.nextLine();
            current.attach(message);
            current.interestOps(SelectionKey.OP_WRITE);
        }
        System.out.println("");
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
