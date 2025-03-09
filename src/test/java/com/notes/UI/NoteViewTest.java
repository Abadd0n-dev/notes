package com.notes.UI;

import com.notes.model.Note;
import com.notes.service.NoteService;
import com.notes.UI.NoteView;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NoteViewTest extends ApplicationTest {

    private NoteView noteView;
    private NoteService noteServiceMock;

    @Override
    public void start(Stage primaryStage) {
        noteView = new NoteView();
        noteServiceMock = Mockito.mock(NoteService.class);
        noteView.noteService = noteServiceMock;
        noteView.start(primaryStage);
    }

    @BeforeEach
    public void setUp() {
        reset(noteServiceMock);
    }

    @Test
    public void testLoadNotes_Success() throws IOException {
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Note 1");
        note1.setContent("Content 1");

        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Note 2");
        note2.setContent("Content 2");

        List<Note> notes = Arrays.asList(note1, note2);
        when(noteServiceMock.getAllNotes()).thenReturn(notes);

        noteView.loadNotes();

        ListView<Note> listView = noteView.listView;
        assertEquals(2, listView.getItems().size());
        assertEquals("Note 1", listView.getItems().get(0).getTitle());
        assertEquals("Note 2", listView.getItems().get(1).getTitle());
    }

    @Test
    public void testAddNote_Success() throws IOException {
        noteView.titleField.setText("New Note");
        noteView.contentArea.setText("New Content");

        noteView.addNote();

        verify(noteServiceMock, times(1)).createNote(any(Note.class));
        verify(noteServiceMock, times(1)).getAllNotes();
    }

    @Test
    public void testDeleteNote_Success() throws IOException {
        noteView.selectedNoteId = 1L;

        noteView.deleteNote();

        verify(noteServiceMock, times(1)).deleteNote(1L);
        verify(noteServiceMock, times(1)).getAllNotes();
    }

    @Test
    public void testClearFields() {
        noteView.titleField.setText("Some Title");
        noteView.contentArea.setText("Some Content");
        noteView.selectedNoteId = 1L;

        noteView.clearFields();

        assertEquals("", noteView.titleField.getText());
        assertEquals("", noteView.contentArea.getText());
        assertNull(noteView.selectedNoteId);
    }
}