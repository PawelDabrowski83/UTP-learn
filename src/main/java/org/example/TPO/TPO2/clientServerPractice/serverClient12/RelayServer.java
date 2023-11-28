package org.example.TPO.TPO2.clientServerPractice.serverClient12;

import java.io.PrintWriter;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class RelayServer {
    private int lineCounter = 0;
    private static final String FILENAME = RelayServer.class.getSimpleName() + "Log.txt";
    public static final String HOST = "localhost";
    public static final int PORT = 65432;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private PrintWriter logger;
}
