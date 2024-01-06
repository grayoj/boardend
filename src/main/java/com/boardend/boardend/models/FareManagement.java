package com.boardend.boardend.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fare_management")
public class FareManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String amount;

    private String rate;

    private String serviceFee;

    private String commissionPercent;

    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDate timestamp;
    public FareManagement() {
    }

    public FareManagement(Long id, String amount, String rate, String serviceFee, String commissionPercent) {
        this.id = id;
        this.amount = amount;
        this.rate = rate;
        this.serviceFee = serviceFee;
        this.commissionPercent = commissionPercent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(String commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }
}
