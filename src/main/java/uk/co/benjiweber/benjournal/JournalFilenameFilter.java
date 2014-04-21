package uk.co.benjiweber.benjournal;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JournalFilenameFilter implements FilenameFilter {
    private final Pattern filenameFormat;
    private final String key;
    private String afterSequence;

    public JournalFilenameFilter(String key, String afterSequence) {
        this.key = key;
        this.afterSequence = afterSequence;
        this.filenameFormat = Pattern.compile("^" + key + ".*-(.*?)\\.log");
    }

    public boolean accept(File dir, String name) {
        Matcher matcher = filenameFormat.matcher(name);
        if (matcher.matches()) {
            String sequenceUpperBound = matcher.group(1);
            return sequenceUpperBound.compareTo(afterSequence) > 0;
        } else {
            return false;
        }
    }
}
