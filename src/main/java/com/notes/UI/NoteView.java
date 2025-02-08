package com.notes.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.notes.server.model.Note;
import com.notes.noteService.NoteService;

import java.io.IOException;
import java.util.List;

public class NoteView extends Application {
    private final NoteService noteService = new NoteService();
    private ListView<Note> listView = new ListView<>();
    private TextField titleField = new TextField();
    private TextArea contentArea = new TextArea();
    private Long selectedNoteId = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Note Manager");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> addNote());

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> deleteNote());

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectNote(newSelection);
            }
        });

        gridPane.add(new Label("Название:"), 0, 0);
        gridPane.add(titleField, 1, 0);
        gridPane.add(new Label("Содержимое:"), 0, 1);
        gridPane.add(contentArea, 1, 1);
        gridPane.add(addButton, 0, 2);
        gridPane.add(deleteButton, 1, 2);
        gridPane.add(listView, 0, 3, 2, 1);

        loadNotes();

        Scene scene = new Scene(gridPane, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadNotes() {
        try {
            List<Note> notes = noteService.getAllNotes();
            listView.getItems().clear();
            listView.getItems().addAll(notes);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Не удалось загрузить заметки.");
        }
    }

    private void selectNote(Note note) {
        selectedNoteId = note.getId();
        titleField.setText(note.getTitle());
        contentArea.setText(note.getContent());
    }

    private void addNote() {
        Note note = new Note();
        note.setTitle(titleField.getText());
        note.setContent(contentArea.getText());

        try {
            noteService.createNote(note);
            loadNotes();
            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Не удалось добавить заметку.");
        }
    }

    private void deleteNote() {
        if (selectedNoteId != null) {
            try {
                noteService.deleteNote(selectedNoteId);
                loadNotes();
                clearFields();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Не удалось удалить заметку.");
            }
        }
    }

    private void clearFields() {
        titleField.clear();
        contentArea.clear();
        selectedNoteId = null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
