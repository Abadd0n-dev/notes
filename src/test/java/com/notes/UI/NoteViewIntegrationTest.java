package com.notes.UI;

import com.notes.model.Note;
import com.notes.service.NoteService;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NoteViewIntegrationTest extends ApplicationTest {
    private NoteService noteServiceMock;
    private NoteView noteView;

    @Override
    public void start(Stage primaryStage) {
        noteServiceMock = Mockito.mock(NoteService.class);
        noteView = new NoteView();
        noteView.noteService = noteServiceMock; // Подменяем оригинальный сервис моком
        primaryStage.setScene(new Scene(noteView.listView, 600, 300));
        primaryStage.show();
    }

    @AfterEach
    public void tearDown() {

        Platform.runLater(() -> {
            if (noteView.listView != null) {
                noteView.listView.getItems().clear();
            }
        });
    }

    @Test
    public void testLoadNotes() throws IOException {
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("First Note");
        note1.setContent("Content 1");

        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Second Note");
        note2.setContent("Content 2");

        List<Note> notes = Arrays.asList(note1, note2);

        when(noteServiceMock.getAllNotes()).thenReturn(notes);

        noteView.loadNotes();

        assertEquals(2, noteView.listView.getItems().size());
        assertEquals("First Note", noteView.listView.getItems().get(0).getTitle());
        assertEquals("Second Note", noteView.listView.getItems().get(1).getTitle());

        assertEquals("Content 1", noteView.listView.getItems().get(0).getContent());
        assertEquals("Content 2", noteView.listView.getItems().get(1).getContent());
    }

    @Test
    public void testAddNote() throws IOException {
        Note newNote = new Note();
        newNote.setTitle("New Note");
        newNote.setContent("New Content");

        when(noteServiceMock.createNote(any(Note.class))).thenReturn(newNote);

        noteView.titleField.setText(newNote.getTitle());
        noteView.contentArea.setText(newNote.getContent());

        noteView.addNote();

        verify(noteServiceMock).createNote(argThat(note ->
                "New Note".equals(note.getTitle()) && "New Content".equals(note.getContent())
        ));

        assertEquals("", noteView.titleField.getText());
        assertEquals("", noteView.contentArea.getText());
    }

    @Test
    public void testDeleteNote() throws IOException {
        Note noteToDelete = new Note();
        noteToDelete.setId(1L);
        noteToDelete.setTitle("Note to delete");
        noteToDelete.setContent("Content");

        noteView.listView.getItems().add(noteToDelete);
        noteView.selectedNoteId = noteToDelete.getId();

        doNothing().when(noteServiceMock).deleteNote(noteToDelete.getId());

        noteView.deleteNote();

        verify(noteServiceMock).deleteNote(noteToDelete.getId());

        assertEquals(0, noteView.listView.getItems().size());
    }
}