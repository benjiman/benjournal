package uk.co.benjiweber.benjournal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JournalWriterBuilder<T> {
    private File file;
    private String key;
    private final Class<T> cls;
    private Serializer<T> serializer;

    public JournalWriterBuilder(Class<T> cls) {
        this.cls = cls;
    }

    public JournalWriterBuilder<T> to(String key) {
        this.key = key;
        return this;
    }

    public static <T> JournalWriterBuilder<T> writing(Class<T> cls) {
        return new JournalWriterBuilder(cls);
    }

    public JournalWriterBuilder<T> location(File file) {
        this.file = file;
        return this;
    }

    public JournalWriter<T> build() {
        return new JournalWriter(key, file, cls, serializer);
    }

    public JournalWriterBuilder<T> serialiseWith(Serializer<T> serializer) {
        this.serializer = serializer;
        return this;
    }


}
