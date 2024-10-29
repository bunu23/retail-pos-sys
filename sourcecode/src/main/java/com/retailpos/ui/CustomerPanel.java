package com.retailpos.ui;

import com.retailpos.integration.DatabaseManager;
import com.retailpos.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class CustomerPanel extends JFrame {
    private JTextField nameField, emailField, phoneField, addressField;
    private JButton addButton, viewButton;
    private JTextArea customerListArea;

    public CustomerPanel() {
        initUI();
    }

    private void initUI() {

        setTitle("Customer Management");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPanel.setBackground(new Color(238, 241, 232));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        gbc.gridx = 0;
        inputPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(nameField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 3;
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(emailField, gbc); // Add Email field

        gbc.gridy = 1;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(200, 30));
        phoneField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 3;
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(200, 30));
        addressField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(addressField, gbc); // Add Address field

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(new Color(238, 241, 232));
        // Buttons with styling
        addButton = new JButton("Add Customer");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(60, 179, 113)); // Green background for the button
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false); // Remove focus ring
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect for the "Add" button
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(46, 139, 87));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(60, 179, 113));
            }
        });

        viewButton = new JButton("View Customers");
        viewButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewButton.setBackground(new Color(52, 152, 219));
        viewButton.setForeground(Color.BLACK);
        viewButton.setFocusPainted(false); // Remove focus ring
        viewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect for the "View" button
        viewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewButton.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewButton.setBackground(new Color(52, 152, 219));
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        customerListArea = new JTextArea();
        customerListArea.setFont(new Font("Arial", Font.PLAIN, 14));
        customerListArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        customerListArea.setEditable(false); // Non-editable for display purposes
        customerListArea.setBackground(new Color(238, 241, 232)); // White background for list area
        customerListArea.setForeground(new Color(50, 50, 50)); // Dark text color

        JScrollPane scrollPane = new JScrollPane(customerListArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around scroll pane

        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCustomers();
            }
        });
    }

    // =====================================================

    private void addCustomer() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        Customer customer = new Customer(0, name, email, phone, address);
        try {
            DatabaseManager.addCustomer(customer);
            JOptionPane.showMessageDialog(this, "Customer added successfully!");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding customer.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    // private void viewCustomers() {
    // try {
    // List<Customer> customers = DatabaseManager.getAllCustomers();
    // customerListArea.setText(""); // Clear existing text
    // for (Customer customer : customers) {
    // customerListArea.append(customer.getName() + " - " + customer.getEmail() +
    // "\n");
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // JOptionPane.showMessageDialog(this, "Error fetching customers.");
    // }
    // }
    private void viewCustomers() {
        try {
            List<Customer> customers = DatabaseManager.getAllCustomers();
            customerListArea.setText(""); // Clear existing text
            for (Customer customer : customers) {
                customerListArea.append("Name: " + customer.getName() + "\n");
                customerListArea.append("Email: " + customer.getEmail() + "\n");
                customerListArea.append("Phone: " + customer.getPhone() + "\n");
                customerListArea.append("Address: " + customer.getAddress() + "\n");
                customerListArea.append("---------------------------\n"); // Separator for clarity
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customers.");
        }
    }
}
