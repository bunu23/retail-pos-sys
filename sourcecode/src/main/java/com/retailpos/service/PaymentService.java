package com.retailpos.service;

import com.retailpos.integration.StripeIntegration;
import com.retailpos.util.EncryptionUtil;

public class PaymentService {
    public boolean processPayment(String cardNumber, String pin, double amount) throws Exception {
        // Encrypt card details
        String encryptedCard = EncryptionUtil.encrypt(cardNumber, "encryptionKey");
        String encryptedPin = EncryptionUtil.encrypt(pin, "encryptionKey");

        // Call Stripe API
        StripeIntegration stripeIntegration = new StripeIntegration();
        String paymentIntent = stripeIntegration.createPaymentIntent((int) (amount * 100));

        // Assume the payment was successful (Stripe handles actual transaction)
        return paymentIntent != null && !paymentIntent.isEmpty();
    }
}



