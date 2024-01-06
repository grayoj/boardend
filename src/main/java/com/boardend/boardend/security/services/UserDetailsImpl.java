package com.boardend.boardend.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.boardend.boardend.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String companyName;

    private String email;

    private String username;

    @JsonIgnore
    private String password;

    private String streetAddress;

    private String companyState;

    private String riderNumber;

    private String accountNumber;

    private String bankName;

    private String cacNumber;

    private Collection<? extends GrantedAuthority> authorities;

    private User user; // Add this field to store the associated User object

    public UserDetailsImpl(Long id, String companyName, String email, String username, String password,
                           String streetAddress, String companyState, String riderNumber, String accountNumber, String bankName, String cacNumber,
                           Collection<? extends GrantedAuthority> authorities, User user) {
        this.id = id;
        this.companyName = companyName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.streetAddress = streetAddress;
        this.companyState = companyState;
        this.riderNumber = riderNumber;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.cacNumber = cacNumber;
        this.authorities = authorities;
        this.user = user;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getCompanyName(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getStreetAddress(),
                user.getCompanyState(),
                user.getRiderNumber(),
                user.getAccountNumber(),
                user.getBankName(),
                user.getCacNumber(),
                authorities,
                user);
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    public void setCacNumber(String cacNumber) {
        this.cacNumber = cacNumber;
    }

    public String getCacNumber() {
        return cacNumber;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getCompanyName() {
        return companyName;
    }


    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCompanyState() {
        return companyState;
    }

    public String getRiderNumber() {
        return riderNumber;
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
