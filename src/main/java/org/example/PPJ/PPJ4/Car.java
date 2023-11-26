//package org.example.PPJ.PPJ4;
//
//public class Car extends Vehicle implements Runnable {
//
//    private String nrRej;
//    private int tankCapacity;   // pojemność baku
//    private int fuel;           // ile jest paliwa?
//
//    public Car(String nr, Person owner, int w, int h, int l,
//               int weight, int tankCap)  {
//        super(owner, w, h, l, weight);
//        nrRej = nr;
//        tankCapacity = tankCap;
//    }
//
//    // Napełnianie baku
//    public void fill(int amount)  {
//        fuel += amount;
//        if (fuel > tankCapacity) fuel = tankCapacity;
//    }
//
//    // Start samochodu
//    public void start()  {
//        if (fuel > 0)   {
//            super.start();
//            new Thread(this).start();  // uruchamiamy wątek zużycia paliwa
//        }
//        else System.out.println("Brak paliwa");
//    }
//
//    // Zatrzymanie samochodu
//    public void stop()  {
//        super.stop();
//    }
//
//    // Kod, który wykonuje się w odrębnym wątku
//    // co 1 sek. czasu programu zużywany jest 1 litr paliwa
//    public void run()  {
//        while(true) {
//            try {
//                Thread.sleep(1000);
//            } catch(InterruptedException exc) { return; }
//            fuel--;
//            System.out.println("Paliwo: " + fuel);  // śledzimy ile jest paliwa
//            if (fuel <= 0) break;    // jeżeli brak paliwa...
//        }
//        System.out.println("Zatrzymanie samochodu z powodu braku paliwa");
//        stop();                     // zatrzymanie samochodu, bo brak paliwa
//    }
//
//    public String toString()  {
//        return "Samochód nr rej " + nrRej + " - " + getState(getState());
//    }
//}
