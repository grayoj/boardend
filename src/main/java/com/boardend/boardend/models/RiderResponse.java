package com.boardend.boardend.models;

import java.util.List;

public class RiderResponse {
    private List<Rider> riderAccounts;
    private long totalRiders;

    public RiderResponse(List<Rider> riderAccounts, long totalRiders) {
        this.riderAccounts = riderAccounts;
        this.totalRiders = totalRiders;
    }

    // Getters and setters

    public List<Rider> getRiderAccounts() {
        return riderAccounts;
    }

    public void setRiderAccounts(List<Rider> riderAccounts) {
        this.riderAccounts = riderAccounts;
    }

    public long getTotalRiders() {
        return totalRiders;
    }

    public void setTotalRiders(long totalRiders) {
        this.totalRiders = totalRiders;
    }
}
