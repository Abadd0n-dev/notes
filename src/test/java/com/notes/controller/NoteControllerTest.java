package com.notes.controller;

import com.notes.model.Note;
import com.notes.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NoteControllerTest {

    @InjectMocks
    private NoteController noteController;

    @Mock
    private NoteRepository noteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllNotes() {
        Note note1 = new Note();
        note1.setContent("Note 1");
        Note note2 = new Note();
        note2.setContent("Note 2");
        List<Note> notes = Arrays.asList(note1, note2);

        when(noteRepository.findAll()).thenReturn(notes);

        List<Note> result = noteController.getAllNotes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Note 1", result.get(0).getContent());
        assertEquals("Note 2", result.get(1).getContent());
    }

    @Test
    public void testCreateNote() {
        Note note = new Note();
        note.setContent("New Note");
        note.setCreatedDate(LocalDateTime.now());

        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note createdNote = noteController.createNote(note);

        assertNotNull(createdNote);
        assertEquals("New Note", createdNote.getContent());
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    public void testDeleteNote() {
        Long noteId = 1L;

        assertDoesNotThrow(() -> noteController.deleteNote(noteId));

        verify(noteRepository, times(1)).deleteById(noteId);
    }
}