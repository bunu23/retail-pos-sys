package com.retailpos.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckoutPanel extends JFrame {

    private JLabel totalAmountLabel;
    private JButton proceedToPaymentButton;
    private double totalAmount;

    public CheckoutPanel(double totalAmount) {
        this.totalAmount = totalAmount;
        initUI();
    }

    private void initUI() {
        setTitle("Checkout");
        setSize(400, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        totalAmountLabel = new JLabel("Total Amount: $" + totalAmount);
        proceedToPaymentButton = new JButton("Proceed to Payment");

        add(totalAmountLabel);
        add(proceedToPaymentButton);

        proceedToPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPinpad();
            }
        });
    }

    private void openPinpad() {
        new PinpadFrame(totalAmount).setVisible(true);
        dispose();
    }
}
