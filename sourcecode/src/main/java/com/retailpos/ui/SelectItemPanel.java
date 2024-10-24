package com.retailpos.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import com.retailpos.integration.DatabaseManager;
import com.retailpos.model.Item;
import com.retailpos.service.TransactionService;

public class SelectItemPanel extends JFrame {
    private JList<String> itemList;
    private JButton addButton;
    private DefaultListModel<String> listModel;
    private List<Item> items;  // To store item objects
    private TransactionService transactionService;

    private JTextArea itemListArea;  // Reference to the main UI cart list
    private JLabel totalLabel;  // Reference to the main UI total label

    public SelectItemPanel(TransactionService transactionService, JTextArea itemListArea, JLabel totalLabel) {
        this.transactionService = transactionService;
        this.itemListArea = itemListArea;  // Assign the references
        this.totalLabel = totalLabel;      // Assign the references
        initUI();
        loadItemsFromDatabase();
    }


    private void initUI() {
        // Set up the main frame
        setTitle("Select Item");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel with dark background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(37, 45, 61)); // Dark background for contrast
        mainPanel.setLayout(new BorderLayout(10, 10)); // Add gaps between components

        // Item list with white background and light border
        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setFont(new Font("Arial", Font.PLAIN, 14));
        itemList.setBackground(Color.WHITE);
        itemList.setForeground(Color.BLACK);
        itemList.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Scroll pane for item list
        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the scroll pane
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // "Add to Cart" button styling
        addButton = new JButton("Add to Cart");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(100, 150, 255)); // Bright blue color
        addButton.setForeground(Color.WHITE); // White text for contrast
        addButton.setFocusPainted(false); // Remove focus ring
        addButton.setBorderPainted(false); // No border for a flat modern look
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor

        // Button hover effect
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(80, 130, 255)); // Darker blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(100, 150, 255)); // Original blue when not hovering
            }
        });

        // Button action listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedItemToCart();
            }
        });

        // Bottom panel for button with contrasting background
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(4, 110, 46, 255)); // Dark green background for contrast
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around button panel
        buttonPanel.add(addButton); // Add button to panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }



    private void loadItemsFromDatabase() {
        try {
            items = DatabaseManager.getAllItems();
            for (Item item : items) {
                listModel.addElement(item.getName() + " - $" + String.format("%.2f", item.getPrice()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading items from database.");
        }
    }

    private void addSelectedItemToCart() {
        int selectedIndex = itemList.getSelectedIndex();
        if (selectedIndex != -1) {
            Item selectedItem = items.get(selectedIndex);
            transactionService.addItem(selectedItem);

            // Update the main UI cart list and total
            itemListArea.append(selectedItem.getName() + " - $" + String.format("%.2f", selectedItem.getPrice()) + "\n");
            totalLabel.setText("Total: $" + String.format("%.2f", transactionService.calculateTotal()));

            JOptionPane.showMessageDialog(this, selectedItem.getName() + " added to cart!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item.");
        }
    }
}
