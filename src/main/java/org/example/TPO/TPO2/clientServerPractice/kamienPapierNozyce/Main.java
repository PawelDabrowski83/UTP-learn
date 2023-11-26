package org.example.TPO.TPO2.clientServerPractice.kamienPapierNozyce;

public class Main {
    public static void main(String[] args) {

        Server server = new Server();

        Client client = new Client(Server.HOST, Server.PORT);

        client.sendFigure(KPN.KAMIEN);
        String response = client.readFigure();
        System.out.println(response);
    }
}
