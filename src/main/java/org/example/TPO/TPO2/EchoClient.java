package org.example.TPO.TPO2;

import java.io.*;
import java.net.*;

public class EchoClient {

    final static int ECHO_PORT = 7;
    private Socket sck;
    private PrintWriter out;
    private BufferedReader in;

    public EchoClient() {}

    public void connect(String host)
            throws UnknownHostException, IOException   {
        sck = new Socket(host, ECHO_PORT);
        in = new BufferedReader (
                new InputStreamReader(sck.getInputStream()));
        out = new PrintWriter (
                new OutputStreamWriter(sck.getOutputStream()), true);
        System.out.println("Połączony z hostem:" + sck.getInetAddress() );
    }

    public void echoMsg(String msg) throws IOException {
        out.println(msg);
        String response = in.readLine();
        System.out.println("Klient: " + msg);
        System.out.println("Serwer: " + response);
    }

    public void disconnect() throws IOException  {
        in.close();
        out.close();
        sck.close();
    }



    public static void main(String[] args) {
        String[] hosts = { "aeneas.mit.edu",
                "cs.toronto.edu",
                "cs.utah.edu",
                "web.mit.edu",
                "boulder.ibm.com",
                "somethin"
        };
        int i = 0;
        EchoClient ec = new EchoClient();
        for (; i<hosts.length; i++) {
            try {
                ec.connect(hosts[i]);
                ec.echoMsg("Dzień dobry!");
                ec.disconnect();
            } catch(UnknownHostException exc) {
                System.out.println("Nieznany host: " + hosts[i]);
            } catch(IOException exc) {
                System.out.println(hosts[i] + " - " + exc);
            }
        }
    }


}
