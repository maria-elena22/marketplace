package com.fcul.marketplace.dto;

public class PaymentConfirmationRequest {
    private String clientSecret;

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
