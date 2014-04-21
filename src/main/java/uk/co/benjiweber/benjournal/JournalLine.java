package uk.co.benjiweber.benjournal;

import java.net.URLDecoder;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JournalLine {
    private final String raw;
    private static final Pattern linePattern = Pattern.compile("^sequence=(.*?)&type=(.*?)&payload=(.*)");
    private final Matcher matcher;

    public JournalLine(String raw) {
        this.raw = raw;
        this.matcher = linePattern.matcher(raw);
        if (!matcher.matches()) throw new IllegalArgumentException("Malformed journal line: " + raw);
    }

    public static Predicate<JournalLine> afterOrEqualTo(String sequence) {
        return line -> line.sequence().compareTo(sequence) >= 0;
    }



    public String sequence() {
        return matcher.group(1);
    }

    public Class<?> type() {
        return unchecked(() -> Class.forName(matcher.group(2)));
    }

    public String value() {
        return URLDecoder.decode(matcher.group(3));
    }


    public String toString() {
        return raw;
    }

    public String raw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JournalLine that = (JournalLine) o;

        if (raw != null ? !raw.equals(that.raw) : that.raw != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return raw != null ? raw.hashCode() : 0;
    }
}
