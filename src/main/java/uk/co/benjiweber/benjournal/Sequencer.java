package uk.co.benjiweber.benjournal;

import static java.lang.Math.random;
import static uk.co.benjiweber.benjournal.Hostname.hostname;

public interface Sequencer {
    default String next() {
        return System.currentTimeMillis() + "" + (int)(random() * 1000) + hostname();
    }
}
