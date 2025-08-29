package com.ibm.securepm.model;

public class PasswordEntry {
    private int id;
    private String title;
    private String username;
    private String encryptedPassword;
    private String url;
    private String notes;
    private long lastModified;

    public PasswordEntry() {}

    public PasswordEntry(String title, String username, String encryptedPassword, String url, String notes) {
        this.title = title;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.url = url;
        this.notes = notes;
        this.lastModified = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
