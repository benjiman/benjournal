package uk.co.benjiweber.benjournal;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.co.benjiweber.expressions.Exceptions.unchecked;

public class JournalReader<T> {

    private final String key;
    private final File location;
    private final Class<T> cls;
    private final Deserialiser<T> deserialiser;
    private static long DELAY_BETWEEN_POLLS = 1000L;

    public JournalReader(String key, File location, Class<T> cls, Deserialiser<T> deserialiser) {
        this.key = key;
        this.location = location;
        this.cls = cls;
        this.deserialiser = deserialiser;
    }

    /**
     * Plays journal forward from specified sequence. When end is reached it blocks until more is available.
     */
    public Iterable<T> playFrom(String sequence) {
        Iterator<JournalLine> journalLines = rawFrom(sequence).iterator();
        InfiniteIterable<T> result = () -> {
            JournalLine next = journalLines.next();
            return deserialiser.deserialize(next.value());
        };
        return result;
    }

    public Iterable<JournalLine> rawFrom(String sequence) {
        return new JournalIterable(sequence, this::rawUntilEndFrom);
    }

    public List<T> playUntilEndFrom(String sequence) {
        return rawUntilEndFrom(sequence).stream()
            .map(line -> deserialiser.deserialize(line.value()))
            .collect(Collectors.toList());
    }

    public List<JournalLine> rawUntilEndFrom(String sequence) {
        return journalFiles(sequence)
                .stream()
                .map(File::toPath)
                .flatMap(path -> unchecked(() -> Files.readAllLines(path).stream()))
                .map(JournalLine::new)
                .filter(JournalLine.afterOrEqualTo(sequence))
                .filter(line -> this.cls.isAssignableFrom(line.type()))
                .collect(Collectors.toList());
    }

    private List<File> journalFiles(String afterSequence) {
        return asList(location.list(new JournalFilenameFilter(key, afterSequence))).stream()
            .map(name -> location.getAbsolutePath() + File.separator + name)
            .map(File::new)
            .collect(toList());
    }

    static class JournalIterable implements InfiniteIterable<JournalLine> {

        private Function<String,List<JournalLine>> journalLineProducer;
        private Iterator<JournalLine> journalLines = Collections.<JournalLine>emptyList().iterator();
        private String lastSequence;
        private boolean first = true;

        public JournalIterable(String initialSequence, Function<String,List<JournalLine>> journalLineProducer) {
            this.lastSequence = initialSequence;
            this.journalLineProducer = journalLineProducer;
        }

        @Override
        public JournalLine next() {
            if (!journalLines.hasNext()) {
                journalLines = journalLineProducer.apply(lastSequence).iterator();
                return next();
            }
            return recordSequence(journalLines.next());
        }

        private JournalLine recordSequence(JournalLine next) {
            if (!first && this.lastSequence.equals(next.sequence())) {
                unchecked(() -> Thread.sleep(DELAY_BETWEEN_POLLS));
                return next();
            }
            first = false;
            this.lastSequence = next.sequence();
            return next;
        }

    }

    interface InfiniteIterable<T> extends Iterable<T>, Iterator<T> {
        default Iterator<T> iterator() { return this; }
        default boolean hasNext() { return true; }
    }

}
