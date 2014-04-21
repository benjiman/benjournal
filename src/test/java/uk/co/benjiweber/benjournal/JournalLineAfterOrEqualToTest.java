package uk.co.benjiweber.benjournal;

import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JournalLineAfterOrEqualToTest {

    Predicate<JournalLine> checker = JournalLine.afterOrEqualTo("1398072542397935lcars");

    @Test public void should_reject_lines_sequenced_before_desired() {
        assertFalse(checker.test(new JournalLine("sequence=1398072541035743lcars&type=uk.co.benjiweber.benjournal.JournalWriterTest.Person&payload=Hello%7C5")));
        assertFalse(checker.test(new JournalLine("sequence=1398072541054504lcars&type=uk.co.benjiweber.benjournal.JournalWriterTest.Person&payload=Hello%7C5")));
    }

    @Test public void should_accept_lines_sequenced_as_desired() {
        assertTrue(checker.test(new JournalLine("sequence=1398072542397935lcars&type=uk.co.benjiweber.benjournal.JournalWriterTest.Person&payload=Hello%7C5")));
    }

    @Test public void should_accept_lines_sequenced_after_desired() {
        assertTrue(checker.test(new JournalLine("sequence=1398072542401317lcars&type=uk.co.benjiweber.benjournal.JournalWriterTest.Person&payload=Hello%7C5")));
        assertTrue(checker.test(new JournalLine("sequence=1398072543858730lcars&type=uk.co.benjiweber.benjournal.JournalWriterTest.Person&payload=Hello%7C5")));
    }
}
