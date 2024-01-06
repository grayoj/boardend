package com.boardend.boardend.payload.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String name;
    private String email;
    private String username;
    private String phone;
    private String streetAddress;
    private String companyName;
    private String companyState;
    private String riderNumber;

    private String vehicleNumber;
    private String status;

    private String accountNumber;

    private String bankName;

    private String cacNumber;
    private List<String> roles;

    // Constructor for users
    public JwtResponse(String accessToken, String refreshToken, Long id, String name, String email, String username, String streetAddress,
                       String companyName, String companyState, String riderNumber, String status, String accountNumber, String bankName, String cacNumber,
                       List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.name = null;
        this.email = email;
        this.username = username;
        this.streetAddress = streetAddress;
        this.companyName = companyName;
        this.companyState = companyState;
        this.riderNumber = riderNumber;
        this.status = status;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.cacNumber = cacNumber;
        this.roles = roles;
    }

    // Constructor for riders
    public JwtResponse(String accessToken, String refreshToken, Long id, String name, String username, String email, String streetAddress,
                       String phone, String password, String vehicleNumber, String companyState, boolean available,
                       List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.streetAddress = streetAddress;
        this.companyName = null;
        this.companyState = null;
        this.riderNumber = null;
        this.vehicleNumber = vehicleNumber;
        this.phone = phone;
        this.status = null;
        this.roles = roles;
    }

    // Constructor for Users
    public JwtResponse(String accessToken, String refreshToken, Long id, String name, String username, String email,
                       String phone,
                       List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
    }


    // Getters and setters

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getRiderNumber() {
        return riderNumber;
    }

    public void setRiderNumber(String riderNumber) {
        this.riderNumber = riderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCacNumber() {
        return cacNumber;
    }

    public void setCacNumber(String cacNumber) {
        this.cacNumber = cacNumber;
    }
}
