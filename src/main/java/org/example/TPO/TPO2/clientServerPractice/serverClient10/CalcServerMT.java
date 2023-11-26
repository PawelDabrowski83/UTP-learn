package org.example.TPO.TPO2.clientServerPractice.serverClient10;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class CalcServerMT {
    private Solvable solver;
    private PrintWriter loggerHandle;
    private int lineCounter = 0;
    private final String FILENAME = "CalcServerMTLog.txt";
    public CalcServerMT(Solvable solver) {
        this.solver = solver;
        operate();
    }

    private void operate() {
        try (PrintWriter logger = new PrintWriter(new FileWriter(FILENAME, true), true)) {
            logToFile(logger, "Starting server.");
            loggerHandle = logger;

            try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                Selector selector = Selector.open()
            ) {
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(
                        new InetSocketAddress(CalcServer.HOST, CalcServer.PORT)
                );
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                logToFile(logger, "Selector registered.");
                acceptConnections(serverSocketChannel, selector);

            }

            logToFile(logger, "Closing server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnections(ServerSocketChannel serverSocketChannel, Selector selector) {
        boolean serverIsRunning = true;
        logToFile(loggerHandle, "Accepting connections.");
        while (serverIsRunning) {
            try {
                logToFile(loggerHandle, "Trying to select something.");
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey current = iter.next();
                    logToFile(loggerHandle, "Selecting a key.");
                    iter.remove();
                    if (current.isAcceptable()) {
                        try(SocketChannel socketChannel = serverSocketChannel.accept()) {
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            continue;
                        }
                    }

                    if (current.isReadable()) {
                        readRequest(current);
                    }

                    if (current.isWritable()) {
                        writeResponse(current);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readRequest(SelectionKey key) throws IOException {
        try (SocketChannel socketChannel = (SocketChannel) key.channel()) {
            logToFile(loggerHandle, "Reading request.");
            if (!socketChannel.isOpen()) {
                return;
            }
            String request = readRequestWithBuffer(key);
            logToFile(loggerHandle, String.format("Received: %s", request));
            socketChannel.register(key.selector(), SelectionKey.OP_WRITE, request);
        };
    }

    private String readRequestWithBuffer(SelectionKey key) throws IOException {
        try (SocketChannel socketChannel = (SocketChannel) key.channel()) {
            StringBuilder sb = new StringBuilder();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            try {
                readLoop:
                while (true) {
                    int n = socketChannel.read(buffer);
                    if (n > 0) {
                        buffer.flip();
                        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                        while (charBuffer.hasRemaining()) {
                            char c = charBuffer.get();
                            if (c == '\r' || c == '\n') {
                                break readLoop;
                            }
                            sb.append(c);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

    }

    private void writeResponse(SelectionKey key) throws IOException {
        try (SocketChannel socketChannel = (SocketChannel) key.channel()) {
            String message = (String) key.attachment();
            message = solver.solve(message);
            logToFile(loggerHandle, String.format("Calculated response: %s", message));
            String messageLn = message + System.lineSeparator();
            socketChannel.write(ByteBuffer.wrap(messageLn.getBytes()));
            logToFile(loggerHandle, message);
            socketChannel.register(key.selector(), SelectionKey.OP_READ);
        }

    }

    private void logToFile(PrintWriter logger, String message) {
        logger.println(
                String.format("%d | %s", lineCounter++, message)
        );
    }

    public static void main(String[] args) {
        new CalcServerMT(new CalcTaskSolver());
    }
}
