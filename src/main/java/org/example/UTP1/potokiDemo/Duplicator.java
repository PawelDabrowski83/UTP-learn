package org.example.UTP1.potokiDemo;

import java.io.*;

public class Duplicator extends Thread {

    PipedReader fromAuthor;    // potok od autora
    PipedWriter[] toWriters;   // potoki do przepisywaczy

    public Duplicator(PipedReader pr,       // potok od autora
                      SpaceToWrite[] space  // na czym piszą pzrepisywacze?
    ) throws IOException {
        fromAuthor = pr;

        int numOfWriters = space.length;      // tylu jest przepisywaczy
        // ile miejsc na których piszą

        // Tworzymy tablicę potoków do przepisywaczy
        toWriters = new PipedWriter[numOfWriters];

        for (int i = 0; i < numOfWriters; i++) { // dla każdego przepisywacza

            // tworzymy potok do niego
            toWriters[i] = new PipedWriter();

            // tworzymy przepisywacza
            // podając: nazwę, z jakiego potoku ma czytać, miejsce gdzie ma pisać
            TxtWriter tw = new TxtWriter("TxtWriter " + (i+1),
                    new PipedReader( toWriters[i]), // połączenie!
                    space[i]);

            // uruchamiamy wątek przepisywacza
            tw.start();
        }
    }

    // Kod wykonywany w wątku Duplikatora
    public void run() {
        try {
            // Buforowanie potoku od autora
            BufferedReader in = new BufferedReader(fromAuthor);

            // czytanie wierszy z potoku od autora
            // i zapisywanie ich do potoków, czytanych przez przepisywaczy
            while (true) {
                String line = in.readLine();
                for (int i = 0; i < toWriters.length; i++) {
                    toWriters[i].write(line);
                    toWriters[i].write('\n');
                }
                if (line.equals("Koniec pracy")) break;
            }
        } catch (IOException exc) { return; }
        System.out.println("Duplikator zakończył działanie");
    }

}
