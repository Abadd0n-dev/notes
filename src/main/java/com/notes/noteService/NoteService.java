package com.notes.noteService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import com.notes.server.model.Note;

import java.io.IOException;
import java.util.List;

public class NoteService {
    private final String API_URL = "http://localhost:8080/api/notes";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Конструктор класса
    public NoteService() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public List<Note> getAllNotes() throws IOException {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return objectMapper.readValue(response.body().string(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Note.class));
        }
    }

    public Note createNote(Note note) throws IOException {
        RequestBody body = RequestBody.create(objectMapper.writeValueAsString(note), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return objectMapper.readValue(response.body().string(), Note.class);
        }
    }

    public void deleteNote(Long id) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + "/" + id)
                .delete()
                .build();
        client.newCall(request).execute();
    }
}
