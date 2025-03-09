package com.notes.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoteTest {

    private Note note;

    @BeforeEach
    public void setUp() {
        note = new Note();
    }

    @Test
    public void testSetAndGetId() {
        Long expectedId = 1L;
        note.setId(expectedId);
        assertEquals(expectedId, note.getId());
    }

    @Test
    public void testSetAndGetTitle() {
        String expectedTitle = "Test Title";
        note.setTitle(expectedTitle);
        assertEquals(expectedTitle, note.getTitle());
    }

    @Test
    public void testSetAndGetContent() {
        String expectedContent = "Test Content";
        note.setContent(expectedContent);
        assertEquals(expectedContent, note.getContent());
    }

    @Test
    public void testSetAndGetCreatedDate() {
        LocalDateTime expectedDate = LocalDateTime.now();
        note.setCreatedDate(expectedDate);
        assertEquals(expectedDate, note.getCreatedDate());
    }
}