package com.retailpos.integration;

import com.retailpos.model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/pos-final-version-db";
    private static final String USER = "root";
    private static final String PASSWORD = "pranam123";

    // Establishing a connection to the MySQL database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }


    // Save transaction data to the database
    public static void saveTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (amount, status, created_at) VALUES (?, ?, NOW())";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setString(2, transaction.getStatus());
            stmt.executeUpdate();
        }
    }

    // Retrieve all transactions
    public static ResultSet getTransactions() throws SQLException {
        String sql = "SELECT * FROM transactions";
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    // Fetch transaction by ID
    public static Transaction getTransactionById(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                return transaction;
            }
        }
        return null;
    }

    // Fetch all items from the database
    public static List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(
                        rs.getString("name"),
                        rs.getString("sku"),
                        rs.getDouble("price")
                );
                items.add(item);
            }
        }
        return items;
    }


    // Add a new customer
    public static void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (name, email, phone, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getAddress());
            stmt.executeUpdate();
        }
    }

    // Fetch all customers
    public static List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
                customers.add(customer);
            }
        }
        return customers;
    }

    // Update inventory
    public static void updateInventory(InventoryItem inventoryItem) throws SQLException {
        String sql = "INSERT INTO inventory (item_id, quantity) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventoryItem.getItemId());
            stmt.setInt(2, inventoryItem.getQuantity());
            stmt.setInt(3, inventoryItem.getQuantity());
            stmt.executeUpdate();
        }
    }

    // Fetch inventory
    public static List<InventoryItem> getInventory() throws SQLException {
        List<InventoryItem> inventory = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                InventoryItem item = new InventoryItem(
                        rs.getInt("id"),
                        rs.getInt("item_id"),
                        rs.getInt("quantity")
                );
                inventory.add(item);
            }
        }
        return inventory;
    }

//    public static String authenticateUser(String username, String password) throws SQLException {
//        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, username);
//            stmt.setString(2, password);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return rs.getString("role");  // Correctly return role
//            } else {
//                return null;  // Invalid credentials
//            }
//        }
//    }


    public static String authenticateUser(String username, String password) throws SQLException {
        String query = "SELECT password, role FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");

                // Check if the stored password is a plain text password
                if (!isHashedPassword(storedPassword)) {
                    // If it's plain text, prompt user to change it
                    return "CHANGE_PASSWORD"; // Indicate that the user needs to change their password
                }

                // Compare hashed password
                if (BCrypt.checkpw(password, storedPassword)) {
                    return role;  // Authentication successful, return role
                }
            }
        }
        return null;  // Invalid credentials
    }

    // Helper method to check if the password is hashed
    private static boolean isHashedPassword(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }

    // Method to update the password in the database
    public static void updatePassword(String username, String newPassword) throws SQLException {
        String hashedPassword = hashPassword(newPassword);
        String query = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    // Method to hash a password using BCrypt
    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }



    // Method to retrieve the user's role by their username
    public static String getRoleByUsername(String username) throws SQLException {
        String query = "SELECT role FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");  // Return the user's role
            }
        }
        return null;  // No user found
    }

    // Method to verify the Remember Me token
    public static boolean isRememberMeTokenValid(String username, String token) throws SQLException {
        String query = "SELECT remember_me_token FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedToken = rs.getString("remember_me_token");
                return storedToken != null && storedToken.equals(token);  // Check if token matches
            }
        }
        return false;  // Invalid token or user not found
    }
}



