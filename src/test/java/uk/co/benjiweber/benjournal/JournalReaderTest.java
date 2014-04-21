package uk.co.benjiweber.benjournal;

import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.benjournal.JournalReaderBuilder.reading;

public class JournalReaderTest {
    List<JournalLine> expectedLines = Arrays.asList(
        "sequence=1398072542401317lcars&type=uk.co.benjiweber.benjournal.Person&payload=World%7C6",
        "sequence=1398072543858730lcars&type=uk.co.benjiweber.benjournal.Person&payload=Hello%7C5",
        "sequence=1398072543862533lcars&type=uk.co.benjiweber.benjournal.Person&payload=World%7C6",
        "sequence=1398072564130618lcars&type=uk.co.benjiweber.benjournal.Person&payload=Hello%7C5",
        "sequence=1398072564134116lcars&type=uk.co.benjiweber.benjournal.Person&payload=World%7C6",
        "sequence=1398072572266536lcars&type=uk.co.benjiweber.benjournal.Person&payload=Hello%7C5",
        "sequence=1398072572276695lcars&type=uk.co.benjiweber.benjournal.Person&payload=World%7C6"
    ).stream().map(JournalLine::new).collect(toList());

    @Test public void should_read_journal() {
        JournalReader<Person> reader =
            reading(Person.class)
                .from("people")
                .location(new File("src/test/resources/journalreader/"))
                .deserialisingWith(str -> new Person("a",1))
                .build();
        assertEquals(expectedLines, reader.rawUntilEndFrom("1398072542401317lcars"));
    }

    @Test public void should_convert_journal_to_required_type() {
        Deserialiser<Person> deserialiser = string -> new Person(string.split("\\|")[0], Integer.parseInt(string.split("\\|")[1]));
        JournalReader<Person> reader =
            reading(Person.class)
                .from("people")
                .location(new File("src/test/resources/journalreader/"))
                .deserialisingWith(deserialiser)
                .build();
        List<Person> persons = reader.playUntilEndFrom("1398072542401317lcars");

        assertEquals(new Person("World",6), persons.get(0));
        assertEquals(new Person("Hello",5), persons.get(1));
    }
}
