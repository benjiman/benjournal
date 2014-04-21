package uk.co.benjiweber.benjournal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JournalWriterBuilder {
    private File file;
    private Map<Class, Serializer> serializers = new HashMap<>();
    private final String key;

    public JournalWriterBuilder(String key) {
        this.key = key;
    }


    public static JournalWriterBuilder journal(String key) {
        return new JournalWriterBuilder(key);
    }

    public JournalWriterBuilder location(File file) {
        this.file = file;
        return this;
    }

    public JournalWriter build() {
        return new JournalWriter(key, file, serializers);
    }

    public <T> JournalWriterBuilder register(Class<T> cls, Serializer<T> serializer) {
        this.serializers.put(cls, serializer);
        return this;
    }


}
