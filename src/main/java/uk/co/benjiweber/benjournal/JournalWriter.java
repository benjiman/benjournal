package uk.co.benjiweber.benjournal;

import ch.qos.logback.classic.Logger;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

public class JournalWriter {

    private final String key;
    private final File location;
    private final Map<Class, Serializer> serializers;
    private final Logger journalLogger;
    private final Sequencer sequencer = new Sequencer() {};

    public JournalWriter(String key, File location, Map<Class, Serializer> serializers) {
        this.key = key;
        this.location = location;
        this.serializers = serializers;
        this.journalLogger = JournalLogFactory.createJournalLogger(key, location.getAbsolutePath());
    }

    public JournalWriter write(Object obj) {
        String line = serializers.get(obj.getClass()).serialize(obj);
        System.out.println("Writing " + line);
        journalLogger.info(
                "sequence=" + sequencer.next() +
                        "&type=" + URLEncoder.encode(obj.getClass().getCanonicalName()) +
                        "&payload=" + URLEncoder.encode(line)
        );
        return this;
    }

}
