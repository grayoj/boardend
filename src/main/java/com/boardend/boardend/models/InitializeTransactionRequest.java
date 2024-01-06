package com.boardend.boardend.models;

public class InitializeTransactionRequest {
    private double amount;
    private String email;
    // Add other request parameters as needed

    // Constructors, getters, and setters


    public InitializeTransactionRequest(double amount, String email) {
        this.amount = amount;
        this.email = email;
    }

    public InitializeTransactionRequest() {

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
