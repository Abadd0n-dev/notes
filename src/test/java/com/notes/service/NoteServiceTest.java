package com.notes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notes.model.Note;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NoteServiceTest {
    private NoteService noteService;
    private OkHttpClient mockClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockClient = mock(OkHttpClient.class);
        objectMapper = new ObjectMapper();
        noteService = new NoteService(mockClient);
    }

    @Test
    public void testGetAllNotes() throws IOException {
        Note note = new Note();
        note.setId(1L);
        note.setContent("Test Note");
        String jsonResponse = objectMapper.writeValueAsString(Collections.singletonList(note));

        Response mockResponse = new Response.Builder()
                .code(200)
                .message("OK")
                .request(new Request.Builder().url("http://localhost:8080/api/notes").build())
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(jsonResponse, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any())).thenReturn(mockCall);

        List<Note> notes = noteService.getAllNotes();

        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals("Test Note", notes.get(0).getContent());
    }

    @Test
    public void testCreateNote() throws IOException {
        Note note = new Note();
        note.setContent("New Note");
        String jsonResponse = objectMapper.writeValueAsString(note);

        Response mockResponse = new Response.Builder()
                .code(201)
                .message("Created")
                .request(new Request.Builder().url("http://localhost:8080/api/notes").build())
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(jsonResponse, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any())).thenReturn(mockCall);

        Note createdNote = noteService.createNote(note);

        assertNotNull(createdNote);
        assertEquals("New Note", createdNote.getContent());
    }

    @Test
    public void testCreateNoteThrowsIOException() throws IOException {
        Note note = new Note();
        note.setContent("New Note");

        Response mockResponse = new Response.Builder()
                .code(400)
                .message("Bad Request")
                .request(new Request.Builder().url("http://localhost:8080/api/notes").build())
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create("", MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any())).thenReturn(mockCall);

        IOException exception = assertThrows(IOException.class, () -> noteService.createNote(note));
        assertEquals("Unexpected code " + mockResponse, exception.getMessage());
    }

    @Test
    public void testDeleteNote() throws IOException {
        Long noteId = 1L;

        Response mockResponse = new Response.Builder()
                .code(204)
                .message("No Content")
                .request(new Request.Builder().url("http://localhost:8080/api/notes/" + noteId).build())
                .protocol(Protocol.HTTP_1_1)
                .build();

        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any())).thenReturn(mockCall);

        assertDoesNotThrow(() -> noteService.deleteNote(noteId));

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(mockClient).newCall(requestCaptor.capture());
        assertEquals("http://localhost:8080/api/notes/" + noteId, requestCaptor.getValue().url().toString());
    }
}