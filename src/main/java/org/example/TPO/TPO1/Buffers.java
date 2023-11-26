package org.example.TPO.TPO1;

import java.nio.*;

public class Buffers {

    static void say(String s) { System.out.println(s); }

    static void showParms(String msg, Buffer b) {
        say("Charakterystyki bufora - " + msg);
        say("capacity  :" + b.capacity());
        say("limit     :" + b.limit());
        say("position  :" + b.position());
        say("remaining :" + b.remaining());
    }

    public static void main(String args[]) {

        // alokacja bufora 10 bajtowego (inicjalnie wartości elementów = 0)
        ByteBuffer b = ByteBuffer.allocate(10);
        showParms("Po utworzeniu", b);

        // Zapis dwóch bajtów do bufora
        b.put((byte) 7).put((byte) 9);
        showParms("Po dodaniu dwóch elementów", b);

        // Przestawienie bufora
        b.flip();
        showParms("Po przestawieniu", b);

        // Teraz możemy czytać wpisane dane
        say("Czytamy pierwszy element: " + b.get());
        showParms("Po pobraniu pierwszego elementu", b);
        say("Czytamy drugi element: " + b.get());
        showParms("Po pobraniu drugiego elementu", b);

        say("Czy możemy jeszcze czytać?");
        try {
            byte x = b.get();
        } catch (BufferUnderflowException exc) {
            say("No, nie - proszę spojrzeć na ostatni limit!");
        }

        // Jeszcze raz odczytajmy dane z bufora
        // w tym celu musimy go przewinąć
        b.rewind();
        showParms("Po przewinięciu", b);

        say("Czytanie wszystkiego, co wpisaliśmy");
        while (b.hasRemaining())
            say("Jest: " + b.get());
    }
}
