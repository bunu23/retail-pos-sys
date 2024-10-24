package com.retailpos.ui;

import com.retailpos.service.PaymentService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PinpadFrame extends JFrame {
    private JTextField cardNumberField;
    private JPasswordField pinField;
    private JButton submitButton;
    private double totalAmount;


    public PinpadFrame(double totalAmount) {
        this.totalAmount = totalAmount;
        initUI();
    }
    private void initUI() {
        // Set up the main frame
        setTitle("Pinpad");
        setSize(350, 180);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Grid layout with spacing
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the form
        formPanel.setBackground(new Color(245, 245, 245)); // Light background color

        // Card Number and PIN fields with padding and border
        cardNumberField = new JTextField();
        cardNumberField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(169, 169, 169), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        pinField = new JPasswordField();
        pinField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(169, 169, 169), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Blue submit button with styling
        submitButton = new JButton("Pay");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(52, 152, 219)); // Blue color for the button
        submitButton.setForeground(Color.WHITE); // White text for contrast
        submitButton.setFocusPainted(false); // Remove focus ring
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor for hover
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding for button
        submitButton.setOpaque(true);

        // Hover effect for submit button
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(41, 128, 185)); // Darker blue on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(52, 152, 219)); // Original blue
            }
        });

        // Adding components to the form panel
        formPanel.add(new JLabel("Card Number:"));
        formPanel.add(cardNumberField);
        formPanel.add(new JLabel("PIN:"));
        formPanel.add(pinField);
        formPanel.add(new JLabel("")); // Empty label for spacing
        formPanel.add(submitButton);

        // Center the form panel in the main frame
        add(formPanel, BorderLayout.CENTER);

        // Button action listener for submit button
        submitButton.addActionListener(e -> processPayment());
    }

    private void processPayment() {
        String cardNumber = cardNumberField.getText();
        String pin = new String(pinField.getPassword());
        PaymentService paymentService = new PaymentService();

        try {
            boolean isSuccessful = paymentService.processPayment(cardNumber, pin, totalAmount);
            if (isSuccessful) {
                JOptionPane.showMessageDialog(this, "Payment Successful");
            } else {
                JOptionPane.showMessageDialog(this, "Payment Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing payment");
        }
        dispose();
    }
}

