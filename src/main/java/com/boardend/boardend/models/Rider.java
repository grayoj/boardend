package com.boardend.boardend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "business_riders")
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(max = 400)
    @Column(name = "fullname")
    private String name;

    @Size(max = 400)
    @Column(name = "phone_number")
    private String phone;

    @Size(max = 400)
    @Column(name = "street_address")
    private String streetAddress;

    @Size(max = 400)
    @Column(name = "email")
    private String email;


    @NotBlank
    @Size(max = 400)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 200)
    private String password;

    @Size(max = 200)
    private String vehicleNumber;

    @Size(max = 120)
    private String companyState;

    @Size(max = 120)
    private String companyName;

    @Column(name = "availability")
    private boolean available;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @JsonIgnore
    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL)
    private List<MobileDelivery> mobiledelivery;


    @Enumerated(EnumType.STRING)
    private Status status;

    public Rider() {

    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "business_rider_roles", joinColumns = @JoinColumn(name = "business_rider_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Rider(String name, String phone, String streetAddress,
                 String email, String username, String password, String vehicleNumber, String companyState, String companyName,
                 boolean available, Status status) {
        this.name = name;
        this.phone = phone;
        this.streetAddress = streetAddress;
        this.email = email;
        this.username = username;
        this.password = password;
        this.vehicleNumber = vehicleNumber;
        this.companyState = companyState;
        this.companyName = companyName;
        this.available = available;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean isAvailable) {
        this.available = isAvailable();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    //    @Override
//    public String toString() {
//        return "business_riders [" +
//                "id=" + id + ", " +
//                "first_name=" + firstName + ", " +
//                "last_name=" + lastName + ", " +
//                "phone_number=" + phoneNumber + ", " +
//                "street_address=" + streetAddress + ", " +
//                "email=" + email + "]";
//    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<MobileDelivery> getMobiledelivery() {
        return mobiledelivery;
    }

    public void setMobiledelivery(List<MobileDelivery> mobiledelivery) {
        this.mobiledelivery = mobiledelivery;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}