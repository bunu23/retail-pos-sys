package com.retailpos.integration;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripeIntegration {

    public StripeIntegration() {
        // Initialize the Stripe API key
        Stripe.apiKey = "your-stripe-secret-key";
    }

    public String createPaymentIntent(int amount) throws StripeException {
        // Create PaymentIntent parameters
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) amount)  // amount is in cents
                .setCurrency("usd")
                .addPaymentMethodType("card")  // Specify card as the payment method
                .build();

        // Create the PaymentIntent
        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();  // Return the client secret for payment confirmation
    }
}
