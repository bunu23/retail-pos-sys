package com.retailpos.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import com.retailpos.integration.DatabaseManager;
import com.retailpos.util.SessionManager;


public class LoginPanel extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPanel() {
        initUI();

        if (checkRememberMe()) {
            autoLogin();
        }
    }

    // Method to check the existence of a Remember Me token
    private boolean checkRememberMe() {
        try {
            // Read the token from the user's local file
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/.pos_remember_me"));
            String line = reader.readLine();
            reader.close();

            if (line != null) {
                String[] parts = line.split(":");  // Format: "username:token"
                if (parts.length == 2) {
                    String username = parts[0];
                    String token = parts[1];

                    // Validate the token with the database
                    return DatabaseManager.isRememberMeTokenValid(username, token);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;  // No valid token found
    }
    // Method to perform auto-login
    private void autoLogin() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/.pos_remember_me"));
            String line = reader.readLine();
            reader.close();

            if (line != null) {
                String[] parts = line.split(":");
                String username = parts[0];  // Retrieve username from the file

                // Fetch the role from the database
                String role = DatabaseManager.getRoleByUsername(username);

                // Log the user in
                if (role != null) {
                    SessionManager.login(username, role);  // Start session
                    loadMainApplication(role);  // Load main application based on role
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    private void initUI() {
        // Set up the main frame
        setTitle("Welcome");
        setSize(350, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the form with padding and custom background color
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Grid layout with spacing
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the form
        formPanel.setBackground(new Color(238, 241, 232)); // Light background color

        // Username and Password fields
        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(169, 169, 169), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(169, 169, 169), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Create a separate panel for the login button to isolate it
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(238, 241, 232)); // Match the background of the form panel
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the button in the panel

        // Blue login button with styling to ensure visibility
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(52, 152, 219)); // Bright blue button
        loginButton.setForeground(Color.WHITE); // White text for contrast
        loginButton.setFocusPainted(false); // Remove focus ring
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor for hover
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2)); // Add border to make it stand out
        loginButton.setOpaque(true); // Ensures button color is fully painted
        loginButton.setBorderPainted(true); // Ensures the border is visible

        // Hover effect for login button
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(11, 51, 243)); // Darker blue on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(11, 51, 243)); // Original blue
            }
        });

        // Adding components to the form panel
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("")); // Empty label for spacing
        formPanel.add(loginButton);

        // Center form panel in the main frame
        add(formPanel, BorderLayout.CENTER);

        // Button action listener for login
        loginButton.addActionListener(e -> handleLogin());
    }



//    private void handleLogin() {
//        String username = usernameField.getText();
//        String password = new String(passwordField.getPassword());
//
//        try {
//            String role = DatabaseManager.authenticateUser(username, password);
//            if (role != null) {
//                loadMainApplication(role);
//            } else {
//                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error during login.");
//        }
//    }



    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            String result = DatabaseManager.authenticateUser(username, password);
            if ("CHANGE_PASSWORD".equals(result)) {
                // Prompt user to change password
                String newPassword = JOptionPane.showInputDialog(this, "Your password needs to be updated. Please enter a new password:");
                if (newPassword != null && !newPassword.isEmpty()) {
                    DatabaseManager.updatePassword(username, newPassword);
                    JOptionPane.showMessageDialog(this, "Password updated successfully. Please log in again.");
                }
            } else if (result != null) {
                SessionManager.login(username, result);
                loadMainApplication(result);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login.");
        }
    }

    private void handleLogout() {
        SessionManager.logout();
        JOptionPane.showMessageDialog(this, "You have been logged out.");
        System.exit(0);  // Exit the app
    }


    //    private void loadMainApplication(String role) {
//        // Dispose of the login frame
//        this.dispose();
//
//        // Load the main POS frame based on the role
//        SwingUtilities.invokeLater(() -> {
//            // Check if the role is passed correctly
//            if (role != null && (role.equals("ADMIN") || role.equals("CASHIER"))) {
//                MainPOSFrame posFrame = new MainPOSFrame(role);  // Pass role to MainPOSFrame
//                posFrame.setVisible(true);
//            } else if (role != null && role.equals("INVENTORY_MANAGER")) {
//                InventoryPanel inventoryFrame = new InventoryPanel();  // Inventory Panel for inventory managers
//                inventoryFrame.setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(null, "Invalid role. Access denied.");
//            }
//        });
//    }
private void loadMainApplication(String role) {
    new MainPOSFrame(role).setVisible(true);
    this.dispose();  // Close the login panel
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPanel loginPanel = new LoginPanel();
            loginPanel.setVisible(true);
        });
    }
}

