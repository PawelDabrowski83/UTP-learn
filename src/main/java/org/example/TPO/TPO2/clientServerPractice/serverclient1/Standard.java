package org.example.TPO.TPO2.clientServerPractice.serverclient1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Standard {

    public static final String PROTOCOL_INIT = "11 HELLO SPOCK" + System.lineSeparator();
    public static final String PROTOCOL_HANDSHAKE = "12 LIVE LONG AND PROSPER" + System.lineSeparator();

    public static final String PROTOCOL_CONFIRM = "13 CONFIRM" + System.lineSeparator();
    public static final String PROTOCOL_END_REQUEST = "56 WANT TO LEAVE NOW" + System.lineSeparator();
    public static final String PROTOCOL_END_GRANTED = "66 CLEARED FOR LEAVING" + System.lineSeparator();
    public static final String PROTOCOL_ERROR = "00 UNKNOWN ERROR" + System.lineSeparator();
    public static final String PROTOCOL_TEMPLATE = "%s %s" + System.lineSeparator();
    public static final int SERVER_PORT = 65432;
    public static final String SERVER_IP = "localhost";

    public static final Map<MESSAGE, String> PROTOCOL= new HashMap<>();

    public enum MESSAGE {

        INIT("11 HELLO SPOCK"),
        HANDSHAKE("12 LIVE LONG AND PROSPER"),
        END_REQUEST("56 WANT TO LEAVE NOW"),
        END_GRANTED("66 CLEARED FOR LEAVING"),
        ERROR("00 UNKNOWN ERROR"),
        CONFIRM("13 CONFIRM"),
        TEMPLATE("%s");

        private final String field;

        MESSAGE(String field) {
            this.field = field;
            PROTOCOL.put(this, this.getMessage());
        }

        public String getMessage() {
            return field + System.lineSeparator();
        }
    }

    public static int getSafeRandom(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to);
    }
}
