package org.example.PPJ.PPJ1;

import java.util.*;
import javax.swing.*;


public class Test {

    public static void msg (String s) {
        JOptionPane.showMessageDialog(null, s);
    }

    public static String ask(String msg, Object data) {
        return JOptionPane.showInputDialog(msg, data);
    }

    public static void main(String[] args) {
        // Ustalenie parametrów kont
        try {
            Account.setInterestRate(5);
            Account.setNormalFee(10);
            Account.setSilverFee(20);
            Account.setGoldFee(40);
        } catch (AccountException exc) {
            msg("Invalid accounts param: " + exc.getMessage());
            return;
        }

        // Konta
        Account ac1 = new Account(AccType.GOLD, "A0000001"),
                ac2 = new Account(AccType.NORMAL, "A0000001");

        // Wplaty, wyplaty, transfery
        String in = "";
        while ((in = ask("Enter: \n" +
                "DEPOSIT ammount - for deposit to A1\n" +
                "WITHDRAW ammount - for withdrawal from A1\n" +
                "TRANSFER ammount - for transfer from A1 to A2", in)) != null) {
            try {
                Scanner sc = new Scanner(in);
                OpType op = OpType.valueOf(sc.next());
                double d = sc.nextDouble();
                switch (op) {
                    case DEPOSIT  : ac1.deposit(d); break;
                    case WITHDRAW : ac1.withdraw(d); break;
                    case TRANSFER : ac1. transfer(ac2, d); break;
                    default: msg("No such operation"); break;
                }
                msg("Accounts:\n" + ac1 + '\n' + ac2);
            } catch(Exception exc) {
                msg("Invalid data\n" + exc.getMessage());
            }
        }

        // Symulacja stałych miesięcznych wpłat-wypłat - jaki stan konta po n miesiącah?
        in = "";

        int m = 0;
        double deposit = 0, withdraw = 0;
        while ((in = ask("Enter: number of months, month deposits, month withdrawals", in)) != null) {
            Scanner sc = new Scanner(in);
            try {
                m = sc.nextInt();
                deposit = sc.nextDouble();
                withdraw = sc.nextDouble();
                for (int i = 1; i <= m; i++) {
                    ac1.deposit(deposit);
                    ac1.withdraw(withdraw);
                    ac1.addInterest();
                    ac1.fee();
                }
                msg("After " + m + " months balance should be " + ac1.getBalance());
            } catch(Exception exc) {
                msg("Invalid data" + exc.getMessage());
            }
        }

    }

}
