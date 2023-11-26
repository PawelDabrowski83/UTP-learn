package org.example.PPJ.PPJ1;

import static javax.swing.JOptionPane.*;
import java.util.*;

public class Toto {

    public static void main(String[] args) {
        String msg = "Podaj magiczne liczby,\n" +
                "nic nie wpisuj = automatyczna inicjacja,\n" +
                "lub wybierz Cancel, by skończyć losowania";

        Random rand;                          // generator

        boolean[] isDrawn = new boolean[49];  // i-ty element tablicy
        // ma wartość true
        // gdy liczba i została wylosowana
        // inicjalnie jest false (żadna nie wylosowana)

        String inp;
        while ((inp = showInputDialog(msg)) != null) {

            if (!inp.equals("")) {  // inicjacja generatora sumą podanych liczb
                Scanner sc = new Scanner(inp);
                long sum = 0;
                while (sc.hasNextInt()) sum += sc.nextInt();
                rand = new Random(sum);
            }
            else rand = new Random();  // gdy nic nie podano - inicjacja czasem

            // Losowanie

            final int ILE = 6;  // liczb do wylosowania
            int k = 0;          // licznik niepowtarzających się liczb

            String out = "";   // tu będzie wynik
            while (k < ILE) {
                int n = rand.nextInt(49);  // losowanie:  0 <= n < 49
                if (isDrawn[n]) continue;  // jeżeli ta liczba już była wylosowana
                else {                     // nie była - bierzemy ją!
                    k++;                     // licznik wziętych + 1
                    isDrawn[n] = true;       // teraz jest naprawdę wylosowana
                    out += " " + (n+1);      // n+1, bo mamy mieć od 1 do 49
                }
            }
            showMessageDialog(null, "Wylosowane liczby:\n" + out);
        }
    }

}
