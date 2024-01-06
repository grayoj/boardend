package com.boardend.boardend.models;

public class InitializeTransactionResponse {
    private boolean status;
    private String message;
    private String authorizationUrl;
    private String accessCode;
    private String reference;
    private int amount;
    private String currency;

    // Constructors
    public InitializeTransactionResponse() {
    }

    public InitializeTransactionResponse(boolean status, String message, String authorizationUrl, String accessCode, String reference, int amount, String currency) {
        this.status = status;
        this.message = message;
        this.authorizationUrl = authorizationUrl;
        this.accessCode = accessCode;
        this.reference = reference;
        this.amount = amount;
        this.currency = currency;
    }

    // Getters and setters
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


}
