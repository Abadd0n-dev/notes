package com.notes.controller;

import com.notes.model.Note;
import com.notes.repository.NoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        noteRepository.deleteAll();
    }

    @Test
    public void testGetAllNotes() throws Exception {
        Note note1 = new Note();
        note1.setTitle("Note 1");
        note1.setContent("Content 1");
        noteRepository.save(note1);

        Note note2 = new Note();
        note2.setTitle("Note 2");
        note2.setContent("Content 2");
        noteRepository.save(note2);

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testCreateNote() throws Exception {
        Note note = new Note();
        note.setTitle("New Note");
        note.setContent("New Content");

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Note"))
                .andExpect(jsonPath("$.content").value("New Content"));
    }

    @Test
    public void testDeleteNote() throws Exception {
        Note note = new Note();
        note.setTitle("Note to delete");
        note.setContent("Content to delete");
        Note savedNote = noteRepository.save(note);

        mockMvc.perform(delete("/api/notes/{id}", savedNote.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}