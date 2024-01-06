package com.boardend.boardend.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_number")
    private String customerNumber;

    @Column(name = "drop_address")
    private String dropAddress;

    @Column(name = "pickup_address")
    private String pickupAddress;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "package_type")
    private String packageType;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "fare")
    private double amount;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "delivered")
    private boolean delivered;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Delivery() {

    }

    public Delivery(String customerName, String customerEmail, String customerNumber,
            String dropAddress, String pickupAddress,
                    String packageName, String packageType,
                    DeliveryStatus deliveryStatus, double amount) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerNumber = customerNumber;
        this.dropAddress = dropAddress;
        this.pickupAddress = pickupAddress;
        this.packageName = packageName;
        this.packageType = packageType;
        this.deliveryStatus = deliveryStatus;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}