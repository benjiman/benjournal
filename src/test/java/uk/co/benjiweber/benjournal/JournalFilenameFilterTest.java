package uk.co.benjiweber.benjournal;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JournalFilenameFilterTest {

    @Test public void should_accept_valid_journal_filenames() {
        assertTrue(
            new JournalFilenameFilter("people", "0398072771812745lcars")
                .accept(new File("/tmp/benjimon/"), "people-201404211029-1398072771812745lcars.log")
        );

        assertTrue(
                new JournalFilenameFilter("people", "0398072771812745lcars")
                        .accept(new File("/tmp/benjimon/"), "people-201404211028-1398072541021127lcars.log")
        );
    }

    @Test public void should_reject_valid_journal_filenames_before_current_sequence() {
        assertFalse(
                new JournalFilenameFilter("people", "9398072771812745lcars")
                        .accept(new File("/tmp/benjimon/"), "people-201404211029-1398072771812745lcars.log")
        );

        assertFalse(
                new JournalFilenameFilter("people", "9398072771812745lcars")
                        .accept(new File("/tmp/benjimon/"), "people-201404211028-1398072541021127lcars.log")
        );
    }

    @Test public void should_reject_journal_filenames_not_ending_in_log() {
        assertFalse(
                new JournalFilenameFilter("people", "0398072771812745lcars")
                        .accept(new File("/tmp/benjimon/"), "people-201404211029-1398072771812745lcars")
        );

        assertFalse(
                new JournalFilenameFilter("people", "0398072771812745lcars")
                        .accept(new File("/tmp/benjimon/"), "people-201404211028-1398072541021127lcars")
        );
    }

    @Test public void should_reject_journal_filenames_not_starting_with_key() {
        assertFalse(
                new JournalFilenameFilter("people", "0398072771812745lcars")
                        .accept(new File("/tmp/benjimon/"), "foo-201404211029-1398072771812745lcars.oog")
        );

        assertFalse(
                new JournalFilenameFilter("people", "0398072771812745lcars")
                        .accept(new File("/tmp/benjimon/"), "foo-201404211028-1398072541021127lcars.log")
        );
    }
}
