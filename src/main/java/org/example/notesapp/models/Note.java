package org.example.notesapp.models;

public class Note {
    private int id;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;

    public Note(int id, String title, String content, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        if (createdAt.equals(updatedAt)) {
            return "Заголовок: " + title + " | Создано: " + createdAt;
        } else {
            return "Заголовок: " + title + " | Обновлено: " + updatedAt;
        }
    }
}