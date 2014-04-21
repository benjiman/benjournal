package uk.co.benjiweber.benjournal;

import java.io.IOException;
import java.net.InetAddress;

public class Hostname {
    public static String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
