package org.example.TPO.TPO2.clientServerPractice.serverClient9;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread tServer = new FileServer();
        Thread tClient = new FileClient("T1", FileServer.HOST, FileServer.PORT);
        tServer.start();
        Thread.sleep(2000);
        tClient.start();
    }
}
