package org.example.PPJ.PPJ1;

public class Account {

    private static double interestRate; // oprocentowanie

    private AccType at;                // typ konta
    private String id;                 // numer konta
    private double balance;            // stan konta

    private static double normalFee;          // opłaty za konto
    private static double silverFee;
    private static double goldFee;

    // Konstruktor
    // - tworzy nowe konto podaneego typu i o podanym numerze
    public Account(AccType at, String id) {
        this.id = id;
        this.at = at;
    }

    // Stan konta
    public double getBalance() {
        return balance;
    }

    // Wplata
    // jezeli < 0 zglaszamy wyjatek (bledny argument)
    // jesli wyjatek nie zostanie obsluzony - program zakonczy dzialania
    public void deposit(double d) throws AccountException {
        if (d < 0) throw new AccountException("Deposit should be >= 0");
        balance += d;
    }

    // Wyplata z konta
    // jeżeli < 0 zgłaszamy wyjątek (błędny argument)
    // jeżeli wyplata przekraca stan konta - wyjątek "Wypłata niedozwolona"
    public void withdraw(double d) throws AccountException  {
        if (d < 0) throw new AccountException("Withdrawal should be >= 0");
        if (balance - d < 0)
            throw new AccountException(
                    "Withdrawal exceeding balance not allowed");
        balance -= d;
    }


    // Przelew z konta na konto
    // Parametry:
    // - account - konto na ktore przelewamy
    // - d - ile przelewamy
    public void transfer(Account account, double d) throws AccountException {
        withdraw(d);
        account.deposit(d);
    }

    // Kumulacja odsetek (jednorazowa, w wysokosci oprocentowania)
    public void addInterest() {
        balance *= (1 + interestRate / 100);
    }

    // Pobranie opłaty za prowadzenie konta
    public void fee() {
        switch (at) {
            case NORMAL : balance -= normalFee; break;
            case SILVER : balance -= silverFee; break;
            case GOLD : balance -= goldFee;
        }
    }

    // Metoda statyczna: ustala oprocentowanie dla wszystkich kont
    public static void setInterestRate(double d) throws AccountException {
        if (d < 0) throw new AccountException("Interest should be >= 0");
        Account.interestRate = d;
    }

    public static void setNormalFee(double normalFee) {
        Account.normalFee = normalFee;
    }

    public static void setSilverFee(double silverFee) {
        Account.silverFee = silverFee;
    }

    public static void setGoldFee(double goldFee) {
        Account.goldFee = goldFee;
    }

    // Informacja o koncie
    public String toString() {
        return id + " account type " + at + " balance " + balance;
    }

}