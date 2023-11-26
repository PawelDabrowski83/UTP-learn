package org.example.TPO.TPO2.clientServerPractice.serverclient1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * Communication model:
 * Client: INIT
 * Server: Confirm
 * Client: HANDSHAKE
 * Server: Confirm
 * Client: Message
 * Server: Confirm
 * Client: WANT_TO_LEAVE
 * Server: CLEARED_FOR_LEAVING
 * Client: CLEARED_FOR_LEAVING
 * Client ends connection
 */
public class Server {
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    public Server() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(Standard.SERVER_IP, Standard.SERVER_PORT));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Serwer uruchomiony i czeka na połączenia.");
        serviceConnections();
    }

    private void serviceConnections(){
        boolean serverIsRunning = true;

        while(serverIsRunning) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.keys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        readRequest(socketChannel);
                        continue;
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private static final int BUFFER_SIZE = 256;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private StringBuffer stringBuffer = new StringBuffer();

    private void readRequest(SocketChannel socketChannel) {
        if (!socketChannel.isOpen()) {
            return;
        }

        stringBuffer.setLength(0);
        byteBuffer.clear();

        try {
            readLoop:
            while(true) {
                int n = socketChannel.read(byteBuffer);
                if (n > 0) {
                    byteBuffer.flip();
                    CharBuffer charBuffer = byteBuffer.asCharBuffer();
                    while (charBuffer.hasRemaining()) {
                        char c = charBuffer.get();
                        if (c == '\r' || c == '\n') {
                            break readLoop;
                        }
                        stringBuffer.append(c);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        handleRequest(socketChannel, stringBuffer.toString());
    }

    private void handleRequest(SocketChannel socketChannel, String command) {
        String clientId = socketChannel.socket().getInetAddress().getHostAddress();
        System.out.println("Połączono " + clientId);


    }

    public static void main(String[] args) {
        new Server();
    }
}
