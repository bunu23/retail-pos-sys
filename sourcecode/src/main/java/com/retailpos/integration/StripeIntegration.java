package com.retailpos.integration;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripeIntegration {

    public StripeIntegration() {

        Stripe.apiKey = "";
    }

    public String createPaymentIntent(int amount) throws StripeException {
        // Create PaymentIntent parameters
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) amount)
                .setCurrency("usd")
                .addPaymentMethodType("card")
                .build();

        // Create the PaymentIntent
        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();
    }
}
