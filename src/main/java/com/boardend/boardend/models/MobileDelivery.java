package com.boardend.boardend.models;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "mobile_delivery")
public class MobileDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "drop_address")
    private String dropAddress;

    @Column(name = "pickup_address")
    private String pickupAddress;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "package_type")
    private String packageType;

    @Column(name = "rider")
    private String deliveryRider;

    @Column(name = "rider_number")
    private String deliveryRiderNumber;

    @Column(name = "fare_amount")
    private double amount;

    @Column(name = "vehicle")
    private String vehicleType;

    @Column(name = "delivered")
    private boolean delivered;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_phone")
    private String receiverPhone;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(name = "additionalStatus")
    @Enumerated(EnumType.STRING)
    private AdditionalStatus additionalStatus;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime deliveryTime;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mobile_user_id")
    private MobileUser user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Rider rider;

    @OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL)
    @JsonIgnore
    private PaymentResponse paymentResponse;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    public MobileDelivery() {

    }



    public MobileDelivery(String dropAddress, String pickupAddress,
                          String packageName, String packageType,
                          String deliveryRider, String deliveryRiderNumber, double amount, String vehicleType,
                          boolean delivered, String additionalInformation, String receiverName, String receiverPhone, DeliveryStatus status, AdditionalStatus additionalStatus, MobileUser user,
                          LocalDateTime deliveryTime, PaymentType paymentType) {
        this.dropAddress = dropAddress;
        this.pickupAddress = pickupAddress;
        this.packageName = packageName;
        this.packageType = packageType;
        this.deliveryRider = deliveryRider;
        this.deliveryRiderNumber = deliveryRiderNumber;
        this.delivered = delivered;
        this.additionalInformation = additionalInformation;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.status = status;
        this.additionalStatus = additionalStatus;
        this.user = user;
        this.deliveryTime = deliveryTime;
        this.amount = amount;
        this.vehicleType = vehicleType;
        this.paymentType = paymentType;
    }

    public long getId() {
        return id;
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

    public String getDeliveryRider() {
        return deliveryRider;
    }

    public void setDeliveryRider(String deliveryRider) {
        this.deliveryRider = deliveryRider;
    }

    public String getDeliveryRiderNumber() {
        return deliveryRiderNumber;
    }

    public void setDeliveryRiderNumber(String deliveryRiderNumber) {
        this.deliveryRiderNumber = deliveryRiderNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean isDelivered) {
        this.delivered = isDelivered;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public AdditionalStatus getAdditionalStatus() {
        return additionalStatus;
    }

    public void setAdditionalStatus(AdditionalStatus additionalStatus) {
        this.additionalStatus = additionalStatus;
    }

    public MobileUser getUser() {
        return user;
    }

    public void setUser(MobileUser user) {
        this.user = user;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public PaymentResponse getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(PaymentResponse paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // @Override
    // public String toString() {
    // return "business_delivery [" +
    // "id=" + id + ", " +
    // "drop_address=" + dropAddress + ", " +
    // "pickup_address=" + pickupAddress + ", " +
    // "packageName=" + packageName + ", " +
    // "packageType=" + packageType + ", " +
    // "rider=" + deliveryRider +
    // "rider_number=" + deliveryRiderNumber +
    // "status=" + status +
    // "timestamp=" + deliveryTime +
    // "]";
    // }

}
