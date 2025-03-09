package com.notes.repository;

import com.notes.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NoteRepositoryIntegrationTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    @Rollback
    void testFindByTitle() {
        Note note1 = new Note();
        note1.setTitle("Unique Title");
        note1.setContent("Content of unique title");
        note1.setCreatedDate(LocalDateTime.now());
        noteRepository.save(note1);

        Note note2 = new Note();
        note2.setTitle("Another Title");
        note2.setContent("Content of another title");
        note2.setCreatedDate(LocalDateTime.now());
        noteRepository.save(note2);

        List<Note> foundNotes = noteRepository.findByTitle("Unique Title");

        assertEquals(1, foundNotes.size());
        assertEquals(note1.getTitle(), foundNotes.get(0).getTitle());
    }

    @Test
    @Rollback
    void testCountNotes() {
        noteRepository.save(createNote("First Note", "This is the first note"));
        noteRepository.save(createNote("Second Note", "This is the second note"));

        long count = noteRepository.count();
        assertEquals(2, count);
    }

    @Test
    @Rollback
    void testDeleteNonExistentNote() {
        assertDoesNotThrow(() -> noteRepository.deleteById(999L));
    }

    @Test
    @Rollback
    void testFindAllNotes() {
        noteRepository.save(createNote("First Note", "This is the first note"));
        noteRepository.save(createNote("Second Note", "This is the second note"));

        List<Note> allNotes = noteRepository.findAll();

        assertEquals(2, allNotes.size());
    }

    private Note createNote(String title, String content) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedDate(LocalDateTime.now());
        return note;
    }
}