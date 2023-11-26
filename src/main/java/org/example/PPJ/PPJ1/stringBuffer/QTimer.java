package org.example.PPJ.PPJ1.stringBuffer;

public class QTimer {

    private final long start;

    public QTimer() {
        System.gc();
        start = System.currentTimeMillis();
    }

    public long getElapsed() {
        return System.currentTimeMillis() - start;
    }
}
