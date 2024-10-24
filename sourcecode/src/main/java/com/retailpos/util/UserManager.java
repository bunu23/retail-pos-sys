package com.retailpos.util;
import com.retailpos.integration.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserManager {
    public void addUser(String username, String password, String role) throws SQLException {
        String hashedPassword = hashPassword(password);
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);  // Store the hashed password
            stmt.setString(3, role);
            stmt.executeUpdate();
        }
    }

    private String hashPassword(String password) {
        // Generate a hashed password using BCrypt
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}