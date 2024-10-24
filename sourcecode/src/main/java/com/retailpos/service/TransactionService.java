package com.retailpos.service;

import com.retailpos.integration.DatabaseManager;
import com.retailpos.model.Item;
import com.retailpos.model.Transaction;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }


    public double calculateTotal() {
        return items.stream().mapToDouble(Item::getPrice).sum();
    }
    public void completeTransaction(double totalAmount, String status) throws SQLException {
        Transaction transaction = new Transaction(0, totalAmount, status, LocalDateTime.now());
        DatabaseManager.saveTransaction(transaction);  // Save transaction to database
    }

}
