package org.example.UTP1;

import java.io.Serializable;

public class TravelSearcher implements Serializable {
    private Travel[] travel;            // tablica podróży
    private int lastIndex = -1;         // indeks ostatnio zapisanej
    private final int MAX_COUNT = 5;    // max rozmiar tablicy
    private boolean sorted = false;     // czy jest posortowana

    // Konstruktor: tworzy tablicę
    public TravelSearcher() {
        travel = new Travel[MAX_COUNT];
    }

    // Metoda add dodaje nowy element do tablicy
    // jeżeli przekrozcono zakres
    // - zgłaszany jest wyjątek własnej klasy NoSpaceForTravelException
    public void add(Travel t) throws NoSpaceForTravelException {
        try {
            lastIndex++;
            travel[lastIndex] = t;
        } catch (ArrayIndexOutOfBoundsException exc) {
            lastIndex--;
            throw new NoSpaceForTravelException("Brakuje miejsca dla dodania podróży");
        }
        sorted = false;
    }

    // Jaki jest ostatni zapisany indeks
    public int getLastIndex() {
        return lastIndex;
    }


    // Wyszukiwanie podróży na podstawie podanego celu (destynacji)
    public Travel search(String dest) {
        if (!sorted) sortByDest();
        return null;
    }

    // Sortowanie - aby można było stosować wyszukiwanie binarne
    private void sortByDest() {
        // ... sortowanie
        sorted = true;
    }

    public String toString() {
        // zwraca spis podróży z tablicy travel (destynacji i cen)
        return "";
    }
}
