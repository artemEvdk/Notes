package org.example.notesapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.notesapp.database.DatabaseHandler;
import org.example.notesapp.models.Note;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainController {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentArea;
    @FXML
    private ListView<Note> notesListView;

    @FXML
    public void initialize() {
        DatabaseHandler.initializeDatabase();
        loadNotesFromDatabase();

        ContextMenu contextMenu = new ContextMenu();

        MenuItem openItem = new MenuItem("Открыть");
        openItem.setOnAction(event -> openNote());

        MenuItem deleteItem = new MenuItem("Удалить");
        deleteItem.setOnAction(event -> deleteNote());

        MenuItem editItem = new MenuItem("Редактировать");
        editItem.setOnAction(event -> editNote());
        contextMenu.getItems().add(editItem);


        contextMenu.getItems().addAll(openItem, deleteItem);

        notesListView.getStylesheets().add(
                getClass().getResource("/org/example/notesapp/views/style.css").toExternalForm()
        );
        notesListView.setContextMenu(contextMenu);

        notesListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                openNote();
            }

        });
        notesListView.setCellFactory(param -> new ListCell<Note>() {
            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    AnchorPane anchorPane = new AnchorPane();

                    TextFlow titleTextFlow = new TextFlow();
                    Text titleText = new Text(note.getTitle());
                    titleTextFlow.getChildren().add(titleText);
                    titleTextFlow.setMaxWidth(350);
                    AnchorPane.setLeftAnchor(titleTextFlow, 0.0);

                    Label dateLabel;

                    if (note.getCreatedAt().equals(note.getUpdatedAt())) {
                        dateLabel = new Label("Создано: " + note.getCreatedAt());
                    } else {
                        dateLabel = new Label("Обновлено: " + note.getUpdatedAt());
                    }

                    AnchorPane.setRightAnchor(dateLabel, 0.0);
                    titleText.getStyleClass().add("note-title");
                    dateLabel.getStyleClass().add("note-date");

                    anchorPane.getChildren().addAll(titleTextFlow, dateLabel);
                    setGraphic(anchorPane);
                }
            }
        });
    }


    private void loadNotesFromDatabase() {
        List<Note> notes = DatabaseHandler.loadNotes();
        notesListView.getItems().setAll(notes);
    }

    private void openNote() {
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            TextArea contentArea = new TextArea(selectedNote.getContent());
            contentArea.setEditable(false);
            contentArea.setWrapText(true);

            ScrollPane scrollPane = new ScrollPane(contentArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(selectedNote.getTitle());
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(scrollPane);
            alert.showAndWait();
        }
    }


    private void deleteNote() {
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            DatabaseHandler.deleteNote(selectedNote.getId());
            notesListView.getItems().remove(selectedNote);
        }
    }

    private void editNote() {
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/notesapp/views/edit_note.fxml"));
                DialogPane editDialogPane = loader.load();

                EditNoteController editNoteController = loader.getController();
                editNoteController.setNote(selectedNote);

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(editDialogPane);
                dialog.setTitle("Редактирование заметки");

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    Note updatedNote = editNoteController.getUpdatedNote();
                    DatabaseHandler.updateNote(updatedNote);
                    loadNotesFromDatabase();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleSave() {
        String title = titleField.getText();
        String content = contentArea.getText();
        if (!title.isEmpty() && !content.isEmpty()) {
            DatabaseHandler.saveNote(title, content);
            loadNotesFromDatabase();
            titleField.clear();
            contentArea.clear();
        }
    }
}