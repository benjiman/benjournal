package uk.co.benjiweber.benjournal;

import ch.qos.logback.classic.Logger;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

public class JournalWriter<T> {

    private final String key;
    private final File location;
    private final Class<T> cls;
    private final Serializer<T> serializer;
    private final Logger journalLogger;
    private final Sequencer sequencer = new Sequencer() {};

    public JournalWriter(String key, File location, Class<T> cls, Serializer<T> serializer) {
        this.key = key;
        this.location = location;
        this.cls = cls;
        this.serializer = serializer;
        this.journalLogger = JournalLogFactory.createJournalLogger(key, location.getAbsolutePath());
    }

    public JournalWriter write(T value) {
        String line = serializer.serialize(value);
        System.out.println("Writing " + line);
        journalLogger.info(
                "sequence=" + sequencer.next() +
                        "&type=" + URLEncoder.encode(value.getClass().getCanonicalName()) +
                        "&payload=" + URLEncoder.encode(line)
        );
        return this;
    }

}
