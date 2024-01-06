package com.boardend.boardend.models;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "business_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "companyName"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username") // Add unique constraint for the username field
})
@Component
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String companyName;


    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 200)
    private String cacNumber;

    @NotBlank
    @Size(max = 200)
    private String streetAddress;

    @NotBlank
    @Size(max = 50)
    private String companyState;

    @Size(max = 200)
    private String riderNumber;

    @Size(max = 200)
    private String accountNumber;

    @Size(max = 200)
    private String bankName;

    @Column(name = "reset_token")
    private String resetToken;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "reset_token_expiration")
    private Instant resetTokenExpiration;

    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "business_user_roles", joinColumns = @JoinColumn(name = "business_user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String companyName, String username, String email, String password, String cacNumber,
                String streetAddress, String companyState, String riderNumber, String accountNumber, String bankName, Status status) {
        this.companyName = companyName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.cacNumber = cacNumber;
        this.streetAddress = streetAddress;
        this.companyState = companyState;
        this.riderNumber = riderNumber;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getCacNumber() {
       return cacNumber;
   }

    public void setCacNumber(String cacNumber) {
        this.cacNumber = cacNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Instant getResetTokenExpiration() {
        return resetTokenExpiration;
    }

    public void setResetTokenExpiration(Instant resetTokenExpiration) {
        this.resetTokenExpiration = resetTokenExpiration;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
