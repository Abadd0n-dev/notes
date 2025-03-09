package com.notes.model;

import com.notes.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class NoteIntegrationTest {

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        noteRepository.deleteAll();
    }

    @Test
    void testCreateNote() {
        Note note = new Note();
        note.setTitle("Test Note");
        note.setContent("This is a test note.");
        note.setCreatedDate(LocalDateTime.now());

        Note savedNote = noteRepository.save(note);

        assertNotNull(savedNote.getId());
        assertEquals("Test Note", savedNote.getTitle());
        assertEquals("This is a test note.", savedNote.getContent());
    }

    @Test
    void testReadNote() {
        Note note = new Note();
        note.setTitle("Read Note");
        note.setContent("This is a note to read.");
        note.setCreatedDate(LocalDateTime.now());
        Note savedNote = noteRepository.save(note);

        Optional<Note> fetchedNote = noteRepository.findById(savedNote.getId());

        assertTrue(fetchedNote.isPresent());
        assertEquals(savedNote.getId(), fetchedNote.get().getId());
        assertEquals("Read Note", fetchedNote.get().getTitle());
    }

    @Test
    void testUpdateNote() {
        Note note = new Note();
        note.setTitle("Original Title");
        note.setContent("Original Content");
        note.setCreatedDate(LocalDateTime.now());
        Note savedNote = noteRepository.save(note);

        savedNote.setTitle("Updated Title");
        savedNote.setContent("Updated Content");
        Note updatedNote = noteRepository.save(savedNote);

        assertEquals("Updated Title", updatedNote.getTitle());
        assertEquals("Updated Content", updatedNote.getContent());
    }

    @Test
    void testDeleteNote() {
        Note note = new Note();
        note.setTitle("Delete Note");
        note.setContent("This note will be deleted.");
        note.setCreatedDate(LocalDateTime.now());
        Note savedNote = noteRepository.save(note);

        noteRepository.deleteById(savedNote.getId());
        Optional<Note> fetchedNote = noteRepository.findById(savedNote.getId());

        assertFalse(fetchedNote.isPresent());
    }
}
