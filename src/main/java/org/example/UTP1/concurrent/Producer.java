package org.example.UTP1.concurrent;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private BlockingQueue<String> queue;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            long timestamp = System.currentTimeMillis();

            try {
                queue.put(String.valueOf(timestamp + " " + queue.remainingCapacity()));
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
