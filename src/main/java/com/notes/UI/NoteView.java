package com.notes.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.notes.model.Note;
import com.notes.service.NoteService;

import java.io.IOException;
import java.util.List;

public class NoteView extends Application {
    public NoteService noteService = new NoteService();
    public ListView<Note> listView = new ListView<>();
    public TextField titleField = new TextField();
    public TextArea contentArea = new TextArea();
    public Long selectedNoteId = null;

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

        listView.setCellFactory(param -> new ListCell<Note>() {
            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setText(null);
                } else {
                    setText(note.getTitle());
                }
            }
        });

        Scene scene = new Scene(gridPane, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadNotes() {
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

    public void addNote() {
        String title = titleField.getText();
        String content = contentArea.getText();

        // Проверка на пустые поля
        if (title.isEmpty() || content.isEmpty()) {
            showAlert("Название и содержимое не могут быть пустыми.");
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);

        try {
            noteService.createNote(note);
            loadNotes();
            clearFields();
            showAlert("Заметка добавлена успешно!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Не удалось добавить заметку.");
        }
    }

    public void deleteNote() {
        if (selectedNoteId != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Подтверждение удаления");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Вы уверены, что хотите удалить эту заметку?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        noteService.deleteNote(selectedNoteId);
                        loadNotes();
                        clearFields();
                        showAlert("Заметка удалена успешно!");
                    } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Не удалось удалить заметку.");
                    }
                }
            });
        } else {
            showAlert("Выберите заметку для удаления.");
        }
    }

    public void clearFields() {
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
