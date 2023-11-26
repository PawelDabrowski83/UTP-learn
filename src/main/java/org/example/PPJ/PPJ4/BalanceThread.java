package org.example.PPJ.PPJ4;

class BalanceThread extends Thread {

    private Balance b;  // referencja do obiektu klasy Balance
    private int count;  // liczba powtórzeń pętli w metodzie run

    public BalanceThread(String name, Balance b, int count) {
        super(name);
        this.b = b;
        this.count = count;
        start();
    }

    public void run() {
        int wynik = 0;
        // W pętli wielokrotnie wywołujemy metodę balance()
        // na rzecz obiektu b klasy Balance.
        // Jeżeli wynik metody jest różny od zera - przerywamy działanie pętli
        synchronized (b) {
            for (int i = 0; i < count; i++) {
                wynik = b.balance();
                if (wynik != 0) break;
            }
        }
        // Pokazujemy wartość zmiennej wynik na wyjściu z metody run()
        System.out.println(Thread.currentThread().getName() +
                " konczy z wynikiem  " + wynik);
    }
}