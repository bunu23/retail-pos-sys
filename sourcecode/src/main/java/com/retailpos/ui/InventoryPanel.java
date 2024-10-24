package com.retailpos.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import com.retailpos.integration.DatabaseManager;
import com.retailpos.model.InventoryItem;


public class InventoryPanel extends JFrame {
    private JTextField itemIdField, quantityField;
    private JButton addButton, viewButton;
    private JTextArea inventoryListArea;

    public InventoryPanel() {
        initUI();
    }

    private void initUI() {
        setTitle("Inventory Management");
        setSize(500, 400); // Increased size for better space utilization
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing between components

        // Input panel with grid layout for labels and text fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        inputPanel.add(new JLabel("Item ID:"), gbc);

        gbc.gridx = 1; // Second column
        itemIdField = new JTextField(15); // Set text field size
        inputPanel.add(itemIdField, gbc);

        gbc.gridx = 0; // First column
        gbc.gridy = 1; // Second row
        inputPanel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1; // Second column
        quantityField = new JTextField(15);
        inputPanel.add(quantityField, gbc);

        // Button panel to separate buttons from input fields
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // FlowLayout for buttons
        addButton = new JButton("Update Inventory");
        viewButton = new JButton("View Inventory");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        // Add input panel and button panel at the top
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER); // Input fields
        northPanel.add(buttonPanel, BorderLayout.SOUTH); // Buttons

        // Text area for inventory list display with scroll pane
        inventoryListArea = new JTextArea(10, 40);
        inventoryListArea.setEditable(false); // Make the text area non-editable
        JScrollPane scrollPane = new JScrollPane(inventoryListArea);

        // Add components to the main frame
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER); // Inventory list in the center

        // Action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInventory();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewInventory();
            }
        });
    }


    private void updateInventory() {
        int itemId = Integer.parseInt(itemIdField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        InventoryItem inventoryItem = new InventoryItem(0, itemId, quantity);
        try {
            DatabaseManager.updateInventory(inventoryItem);
            JOptionPane.showMessageDialog(this, "Inventory updated successfully!");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating inventory.");
        }
    }

    private void clearFields() {
        itemIdField.setText("");
        quantityField.setText("");
    }

    private void viewInventory() {
        try {
            List<InventoryItem> inventoryItems = DatabaseManager.getInventory();
            inventoryListArea.setText("");  // Clear existing text
            for (InventoryItem item : inventoryItems) {
                inventoryListArea.append("Item ID: " + item.getItemId() + " - Quantity: " + item.getQuantity() + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching inventory.");
        }
    }
}

