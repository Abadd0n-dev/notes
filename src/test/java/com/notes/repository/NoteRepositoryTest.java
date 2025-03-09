package com.notes.repository;

import com.notes.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    private Note note;

    @BeforeEach
    public void setUp() {
        note = new Note();
        note.setTitle("Test Title");
        note.setContent("Test Content");
        note.setCreatedDate(LocalDateTime.now());
    }

    @Test
    public void testSaveNote() {
        Note savedNote = noteRepository.save(note);
        assertThat(savedNote).isNotNull();
        assertThat(savedNote.getId()).isNotNull(); // ID должен быть сгенерирован
        assertThat(savedNote.getTitle()).isEqualTo(note.getTitle());
        assertThat(savedNote.getContent()).isEqualTo(note.getContent());
    }

    @Test
    public void testFindById() {
        Note savedNote = noteRepository.save(note);
        Optional<Note> foundNote = noteRepository.findById(savedNote.getId());
        assertThat(foundNote).isPresent();
        assertThat(foundNote.get().getTitle()).isEqualTo(note.getTitle());
    }

    @Test
    public void testFindAll() {
        noteRepository.save(note);
        Note anotherNote = new Note();
        anotherNote.setTitle("Another Title");
        anotherNote.setContent("Another Content");
        anotherNote.setCreatedDate(LocalDateTime.now());
        noteRepository.save(anotherNote);

        assertThat(noteRepository.findAll()).hasSize(2);
    }

    @Test
    public void testDeleteById() {
        Note savedNote = noteRepository.save(note);
        noteRepository.deleteById(savedNote.getId());
        Optional<Note> deletedNote = noteRepository.findById(savedNote.getId());
        assertThat(deletedNote).isNotPresent();
    }
}