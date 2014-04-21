package uk.co.benjiweber.benjournal;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.benjournal.JournalWriterBuilder.writing;

public class JournalWriterTest {

    @Test public void should_write_journal_entries() throws IOException {
        Person persona = new Person("Hello", 5);
        Person personb = new Person("World", 6);

        File journalFile = new File("src/test/resources/journalreader/people");
        journalFile.delete();

        JournalWriter journal = writing(Person.class)
                .from("people")
                .location(new File("src/test/resources/journalreader/"))
                .serialiseWith(p -> p.name + "|" + p.age)
                .build();

        journal.write(persona);
        journal.write(personb);
        assertEquals(Arrays.asList("Hello|5","World|6"), contentsOf(journalFile));


    }

    private List<String> contentsOf(File journalFile) throws IOException {
        return Files.readAllLines(journalFile.toPath()).stream()
                .map(JournalLine::new)
                .map(JournalLine::value)
                .collect(Collectors.toList());
    }


}
