package org.example.PPJ.PPJ4;

public class Main {
    public static void main(String[] args) {
        ThreadGroup threadGroup = new ThreadGroup("Ala");
        Timer timer = new Timer();
        Thread thread = new Thread(timer, "Ala");
        thread.start();
        new Thread(new Timer(), "Ala").start();
        new Thread(new Timer(), "Ala").start();
    }
}
