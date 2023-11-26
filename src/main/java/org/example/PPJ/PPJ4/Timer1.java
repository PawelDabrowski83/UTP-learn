package org.example.PPJ.PPJ4;

import java.util.*;

// Klasa definiująca zadanie do wykonania
// tu będzie to wypisywanie komunikatów

class Message extends TimerTask {

    private String msg;        // komunikat
    private boolean showDate;  // czy pokazać datę

    public Message(String s, boolean show) {
        msg = s;
        showDate = show;
    }

    // Ten kod będzie wykonywany przez Timer
    // według charakterystyk czasowych podanych w metodzie schedule
    public void run() {
        String msg1 = msg;
        if (showDate) msg1 = "Jest " + new Date() + '\n' + msg;
        System.out.println(msg1);
    }
}

// Test
public class Timer1 {

    public static void main(String[] args) {

        // Utworzenie zadań
        Message msgTask1 = new Message("Przypominam o podlaniu kwiatów!", true),
                msgTask2 = new Message("I zakończ ten program!", false);

        // Czas rozpoczęcia drugiegi zadania:
        // za trzy sekundy od teraz
        long taskTime2 = System.currentTimeMillis() + 3000;

        // Utworzenie timera
        java.util.Timer tm = new java.util.Timer();

        // Zlecenie pierwszego zadania do wykonania
        // Ma się zacząć JUŻ - podano 0 jako moment startu
        // i powtarzać się co 2 sekundy
        tm.schedule(msgTask1, 0, 2000);

        // Zlecenie drugiego zadania do wykonania
        // Zacznie się w momencie określanym przez datę
        // - utworzony obiekt Date; tu - zgodnie z czasem timeTask2
        // ale mogłoby to być np. 11 lipca o 9 rano
        tm.schedule(msgTask2, new Date(taskTime2), 2000);

        // Po twierdzącej odpowiedzi na to pytanie ...
        int rc = javax.swing.JOptionPane.showConfirmDialog(null,
                "Czy kwiaty podlane?");
        // ... kończymy działanie zadania msgTask1
        // do czego służy metoda cancel():
        if (rc == 0) msgTask1.cancel();

        // zadanie 2 nadal działa
        javax.swing.JOptionPane.showMessageDialog(null, "Kończyć trzeba");

        // teraz kończymy wszystko
        System.exit(0);
    }

}
