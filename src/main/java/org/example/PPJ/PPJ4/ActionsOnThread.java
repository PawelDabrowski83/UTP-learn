package org.example.PPJ.PPJ4;

import javax.swing.*;

class SomeThread extends Thread {

    volatile boolean stopped = false;
    volatile boolean suspended = false;

    public void run() {

        double d = 0;
        while(!stopped) {
            try {
                synchronized(this) {
                    while (suspended) wait();
                }
            } catch (InterruptedException exc) {
                System.out.println("Obsluga przerwania watku w stanie wait");
                System.out.println("Co powie isInterrupted() ?: "+ isInterrupted());
            }
            if (suspended) System.out.println("Watek wstrzymany na wartosci " + d);
            else System.out.println(++d);
        }
    }

    public void stopThread() {
        stopped = true;
    }

    public void suspendThread() {
        suspended = true;
    }

    public void resumeThread() {
        suspended = false;
        synchronized(this) {
            notify();
        }
    }

}

public class ActionsOnThread {

    public static void main(String args[]) {
        String msg = "I = interrupt\n" +
                "E = end\n" +
                "S = suspend\n" +
                "R = resume\n" +
                "N = new start";

        SomeThread t = new SomeThread();
        t.start();
        String cmd;
        while ((cmd = JOptionPane.showInputDialog(msg)) != null) {
            char c = cmd.charAt(0);
            switch (c) {
                case 'I' : t.interrupt(); break;
                case 'E' : t.stopThread(); break;
                case 'S' : t.suspendThread(); break;
                case 'R' : t.resumeThread(); break;
                case 'N' : if (t.isAlive())
                    JOptionPane.showMessageDialog(null, "Thread alive!!!");
                else {
                    t = new SomeThread();
                    t.start();
                }
                    break;
                default  : break;
            }
            JOptionPane.showMessageDialog(null,
                    "Command " + cmd + " executed.\n" +
                            "Thread alive  ? " + (t.isAlive() ? "Y\n" : "N\n") +
                            "Thread interrupted ? " + (t.isInterrupted() ? "Y\n" : "N")
            );
        }
        System.exit(0);
    }
}
