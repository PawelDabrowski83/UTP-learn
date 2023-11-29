package org.example.TPO.TPO2.clientServerPractice.serverClient12;

import java.util.concurrent.ThreadLocalRandom;

public class RelayGenerator {
    private static final String[] MESSAGES = new String[]{
           "Także ten", "Hyc!", "No właśnie, właśnie", "...", "To już lepiej nie", "No chyba że tak",
           "Generalnie to tak, ale...", "Przynajmniej raz", "Prawda?", "Nie mam pojęcia", "Ano", "jo jo",
           "U nas to jest niemożliwe", "Nawzajem", "Zielony dwa razy się skrada", "Teraz to ich kolej",
           "Nazajutrz będzie lepiej", "Lepiej to już było", "A u lekarza kolejka", "No i co zrobisz jak nie zrobisz"
    };

    public static String[] generateMessages() {
        int limit = MESSAGES.length;
        int wordCount = ThreadLocalRandom.current().nextInt(limit);
        String[] result = new String[wordCount];
        for (int i = 0; i < wordCount; i++) {
            result[i] = MESSAGES[ThreadLocalRandom.current().nextInt(limit)];
        }
        return result;
    }
}
