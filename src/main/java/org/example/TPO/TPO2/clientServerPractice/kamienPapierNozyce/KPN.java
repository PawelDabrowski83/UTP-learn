package org.example.TPO.TPO2.clientServerPractice.kamienPapierNozyce;

public enum KPN {
    KAMIEN, PAPIER, NOZYCE, NULL;

    public static KPN beats(KPN item) {
        if (item.equals(NULL)) {
            return NULL;
        }
        return item.equals(KAMIEN)  ? PAPIER :
                item.equals(PAPIER) ? NOZYCE :
                        KAMIEN;
    }
}
