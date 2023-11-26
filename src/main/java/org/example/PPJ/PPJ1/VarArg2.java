package org.example.PPJ.PPJ1;

public class VarArg2 {

    public static long sum(int ... args) {
        long sum = 0;
        for (int n : args) {
            sum += n;
        }
        return sum;
    }

    public static void main(String[] args) {
        long suma = sum( 1, 2, 3 );
        System.out.println(suma);
        suma = sum( 10, 21 );
        System.out.println(suma);
        suma = sum(  );
        System.out.println(suma);
    }

}
