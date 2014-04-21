package uk.co.benjiweber.benjournal;

import java.io.File;

public class JournalReaderBuilder<T> {
    private File file;
    private String key;
    private final Class<T> type;
    private Deserialiser<T> deserialiser;

    public JournalReaderBuilder(Class<T> type) {
        this.type = type;
    }

    public JournalReaderBuilder<T> from(String key) {
        this.key = key;
        return this;
    }

    public static <T> JournalReaderBuilder<T> reading(Class<T> cls) {
        return new JournalReaderBuilder<T>(cls);
    }

    public JournalReaderBuilder<T> location(File file) {
        this.file = file;
        return this;
    }

    public JournalReader<T> build() {
        return new JournalReader<T>(key, file, type, deserialiser);
    }

    public JournalReaderBuilder<T> deserialisingWith(Deserialiser<T> deserialiser) {
        this.deserialiser = deserialiser;
        return this;
    }
}
