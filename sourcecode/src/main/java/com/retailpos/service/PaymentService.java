package com.retailpos.service;

import com.retailpos.integration.StripeIntegration;
import com.retailpos.util.EncryptionUtil;

public class PaymentService {
    public boolean processPayment(String cardNumber, String pin, double amount) throws Exception {

        String encryptedCard = EncryptionUtil.encrypt(cardNumber, "encryptionKey");
        String encryptedPin = EncryptionUtil.encrypt(pin, "encryptionKey");

        StripeIntegration stripeIntegration = new StripeIntegration();
        String paymentIntent = stripeIntegration.createPaymentIntent((int) (amount * 100));

        return paymentIntent != null && !paymentIntent.isEmpty();
    }
}
