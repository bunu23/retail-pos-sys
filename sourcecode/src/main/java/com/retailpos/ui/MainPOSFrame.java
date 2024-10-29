package com.retailpos.ui;

import com.retailpos.util.SessionManager;
import com.retailpos.service.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPOSFrame extends JFrame {

    private JTextArea itemListArea;
    private JButton addItemButton;
    private JButton checkoutButton;
    private JLabel totalLabel;
    private TransactionService transactionService;
    private JButton customerButton;
    private JButton inventoryButton;
    private String userRole;

    public MainPOSFrame(String role) {
        this.userRole = role;
        System.out.println("User role set to: " + userRole);

        if (!SessionManager.isSessionValid()) {
            JOptionPane.showMessageDialog(this, "Session expired. Please log in again.");
            System.exit(0);
        }

        transactionService = new TransactionService();
        initUI();

    }

    private void initUI() {
        // Set up the main frame
        setTitle("POS Interface");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(255, 255, 255));

        // Initialize UI components with custom fonts and colors
        itemListArea = new JTextArea(10, 40);
        itemListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        itemListArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        itemListArea.setBackground(new Color(7, 253, 187, 19)); // Background

        // Buttons with modern color palette
        addItemButton = createStyledButton("Add Item", new Color(255, 255, 255), Color.WHITE);
        checkoutButton = createStyledButton("Checkout", new Color(46, 204, 113), Color.WHITE);
        customerButton = createStyledButton("Manage Customers", new Color(255, 165, 0), Color.WHITE);
        inventoryButton = createStyledButton("Manage Inventory", new Color(255, 99, 71), Color.WHITE);

        // Total label with bold font and color for visibility
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalLabel.setForeground(new Color(255, 255, 255));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Modern button panel layout with spacing and padding
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10)); // Add spacing between buttons
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        buttonPanel.setBackground(new Color(28, 26, 26)); // Light background to blend with the frame

        // Adding buttons based on user roles
        buttonPanel.add(addItemButton);
        buttonPanel.add(checkoutButton);
        if (userRole.equals("ADMIN") || userRole.equals("CASHIER")) {
            buttonPanel.add(customerButton);
        }
        if (userRole.equals("ADMIN") || userRole.equals("INVENTORY_MANAGER")) {
            buttonPanel.add(inventoryButton);
        }

        // Add components to the frame
        add(new JScrollPane(itemListArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);

        // Button action listeners
        addItemButton.addActionListener(e -> openItemSelection());
        checkoutButton.addActionListener(e -> openCheckout());
        customerButton.addActionListener(e -> openCustomerManagement());
        inventoryButton.addActionListener(e -> openInventoryManagement());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        bottomPanel.setBackground(new Color(28, 26, 26));
        // Logout Button with hover effect
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoutButton.setBackground(new Color(192, 57, 43));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding for a wider button
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add a hover effect for the logout button
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(231, 76, 60));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(192, 57, 43));
            }
        });

        logoutButton.addActionListener(e -> handleLogout());

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(logoutButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.logout(); // Clear session
            dispose(); // Close the current window

            // Optionally redirect to the login page
            SwingUtilities.invokeLater(() -> {
                new LoginPanel().setVisible(true);
            });
        }
    }

    // Method to create styled buttons
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); // Button border
        return button;
    }

    private void openItemSelection() {
        new SelectItemPanel(transactionService, itemListArea, totalLabel).setVisible(true);
    }

    private void openCustomerManagement() {
        new CustomerPanel().setVisible(true);
    }

    private void openInventoryManagement() {
        new InventoryPanel().setVisible(true);
    }

    private void openCheckout() {
        new CheckoutPanel(transactionService.calculateTotal()).setVisible(true);
    }

    // public static void main(String[] args) {
    // SwingUtilities.invokeLater(() -> {
    // MainPOSFrame posFrame = new MainPOSFrame();
    // posFrame.setVisible(true);
    // });
    // }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainPOSFrame posFrame = new MainPOSFrame("ADMIN"); // Hardcoded for testing, replace with dynamic role
            posFrame.setVisible(true);
        });
    }

}
