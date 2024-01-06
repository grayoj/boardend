package com.boardend.boardend.models;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payment_response")
public class PaymentResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "status")
    private boolean status;

    @Column(name = "message")
    private String message;

    @Column(name = "authorization_url")
    private String authorizationUrl;

    @Column(name = "access_code")
    private String accessCode;

    @Column(name = "reference")
    private String reference;

    @Column(name = "amount")
    private double amount;

    @Column(name = "currency")
    private String currency;

    @OneToOne
    @JoinColumn(name = "mobile_delivery_id")
    private MobileDelivery delivery;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATE")
    private LocalDate createdAt;

    public PaymentResponse () {

    }
    public PaymentResponse(boolean status, String message, String authorizationUrl, String accessCode, String reference, int amount, String currency, Delivery delivery, PaymentType paymentType, LocalDate createdAt) {
        this.status = status;
        this.message = message;
        this.authorizationUrl = authorizationUrl;
        this.accessCode = accessCode;
        this.reference = reference;
        this.amount = amount;
        this.currency = currency;
        this.paymentType = paymentType;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public MobileDelivery getDelivery() {
        return delivery;
    }

    public void setDelivery(MobileDelivery delivery) {
        this.delivery = delivery;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
