package org.example.UTP1.concurrent;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(15);

        Runnable producer = new Producer(queue);
        Runnable consumer = new Consumer(queue);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 15; i++) {
            int coin = ThreadLocalRandom.current().nextInt(0, 20);
            Thread thread;
            if (coin <10) {
                thread = new Thread(producer);
            } else {
                thread = new Thread(consumer);
            }
            executorService.submit(thread);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Sleep exception!");
            }
        }
        executorService.shutdown();



    }
}
