package org.example.notesapp.database;

import org.example.notesapp.models.Note;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String URL = "jdbc:sqlite:notes.db";

    private static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к БД: " + e.getMessage());
            return null;
        }
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS notes (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "title TEXT NOT NULL,\n"
                + "content TEXT NOT NULL,\n"
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n"
                + "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void saveNote(String title, String content) {
        String sql = "INSERT INTO notes(title, content) VALUES(?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Note> loadNotes() {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT id, title, content, created_at, updated_at FROM notes";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                notes.add(new Note(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return notes;
    }

    public static void deleteNote(int id) {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateNote(Note note) {
        String sql = "UPDATE notes SET title = ?, content = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, note.getTitle());
            pstmt.setString(2, note.getContent());
            pstmt.setInt(3, note.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}