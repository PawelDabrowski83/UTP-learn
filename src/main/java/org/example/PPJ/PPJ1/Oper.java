package org.example.PPJ.PPJ1;

import java.util.*;
import static javax.swing.JOptionPane.*;

public class Oper {

    public static void main(String[] args) {

        String normalQuest = "Liczba1 op Liczba2",     // normalny komunikat
                errorQuest = "Wadliwe dane. Popraw.\n", // komunikat w przypadku błędu
                quest = normalQuest;

        String expr = "";                              // wyrażenie do obliczenia

        while ((expr = showInputDialog(quest, expr)) != null) {

            StringTokenizer st = new StringTokenizer(expr);

            if (st.countTokens() != 3) {  // jeżeli za mało lub za dużo symboli
                quest = errorQuest;
                continue;
            }

            String snum1 = st.nextToken(),   // pierwsza liczba (napisowo)
                    sop  = st.nextToken(),    // symbol operatora
                    snum2 = st.nextToken();   // druga liczba (napisowo)

            int num1 = 0, num2 = 0, res = 0; // liczbt do obliczeń i wynik

            try {
                num1 = Integer.parseInt(snum1);
                num2 = Integer.parseInt(snum2);
            } catch (NumberFormatException exc) { // jeżeli napisy nie są liczbami całkowitymi
                quest = errorQuest;               // komunikat o błędzie
                continue;
            }

            char op = sop.charAt(0);

            // jezeli napis oznaczający operator za długi (np. ktoś wprowadził +*)
            // lub gdy w ilorazie dzialnik jest zerem - błąd
            if (sop.length() != 1 || (op == '/' && num2 == 0)) {
                quest = errorQuest;
                continue;
            }

            switch (op) {
                case '+' : res = num1 + num2; break;
                case '-' : res = num1 - num2; break;
                case '*' : res = num1 * num2; break;
                case '/' : res = num1 / num2; break;
                default: {                           // wadliwy operator
                    quest = errorQuest;
                    continue;
                }
            }
            showMessageDialog(null, "Wynik = " + res);
            quest = normalQuest;
            expr = "";            // w kolejnym dialogu inicjalny tekst ma być pusty
        }

    }

}
