package com.ibm.securepm.dao;

import com.ibm.securepm.model.PasswordEntry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordDAO {
    private static final String DB_URL = "jdbc:sqlite:passwords.db";
    
    public PasswordDAO() {
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (Statement stmt = conn.createStatement()) {
                // Create passwords table if it doesn't exist
                stmt.execute(
                    "CREATE TABLE IF NOT EXISTS passwords (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "username TEXT," +
                    "encrypted_password TEXT NOT NULL," +
                    "url TEXT," +
                    "notes TEXT," +
                    "last_modified INTEGER" +
                    ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    public void save(PasswordEntry entry) {
        String sql = "INSERT INTO passwords (title, username, encrypted_password, url, notes, last_modified) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entry.getTitle());
            pstmt.setString(2, entry.getUsername());
            pstmt.setString(3, entry.getEncryptedPassword());
            pstmt.setString(4, entry.getUrl());
            pstmt.setString(5, entry.getNotes());
            pstmt.setLong(6, System.currentTimeMillis());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save password entry", e);
        }
    }
    
    public void update(PasswordEntry entry) {
        String sql = "UPDATE passwords " +
            "SET title = ?, username = ?, encrypted_password = ?, url = ?, notes = ?, last_modified = ? " +
            "WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entry.getTitle());
            pstmt.setString(2, entry.getUsername());
            pstmt.setString(3, entry.getEncryptedPassword());
            pstmt.setString(4, entry.getUrl());
            pstmt.setString(5, entry.getNotes());
            pstmt.setLong(6, System.currentTimeMillis());
            pstmt.setInt(7, entry.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update password entry", e);
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM passwords WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete password entry", e);
        }
    }
    
    public PasswordEntry findById(int id) {
        String sql = "SELECT * FROM passwords WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPasswordEntry(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find password entry", e);
        }
        return null;
    }
    
    public List<PasswordEntry> findAll() {
        String sql = "SELECT * FROM passwords ORDER BY title";
        List<PasswordEntry> entries = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                entries.add(mapResultSetToPasswordEntry(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch password entries", e);
        }
        return entries;
    }
    
    private PasswordEntry mapResultSetToPasswordEntry(ResultSet rs) throws SQLException {
        PasswordEntry entry = new PasswordEntry();
        entry.setId(rs.getInt("id"));
        entry.setTitle(rs.getString("title"));
        entry.setUsername(rs.getString("username"));
        entry.setEncryptedPassword(rs.getString("encrypted_password"));
        entry.setUrl(rs.getString("url"));
        entry.setNotes(rs.getString("notes"));
        entry.setLastModified(rs.getLong("last_modified"));
        return entry;
    }
}
