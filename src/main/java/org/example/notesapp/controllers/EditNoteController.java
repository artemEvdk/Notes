package org.example.notesapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.notesapp.models.Note;

public class EditNoteController {
    @FXML private TextField titleField;
    @FXML private TextArea contentArea;

    private Note note;

    public void setNote(Note note) {
        this.note = note;
        titleField.setText(note.getTitle());
        contentArea.setText(note.getContent());
    }

    public Note getUpdatedNote() {
        note.setTitle(titleField.getText());
        note.setContent(contentArea.getText());
        return note;
    }
}