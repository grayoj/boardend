package com.boardend.boardend.security.services;

        import java.util.Collection;
        import java.util.List;
        import java.util.Objects;
        import java.util.stream.Collectors;

        import org.springframework.security.core.GrantedAuthority;
        import org.springframework.security.core.authority.SimpleGrantedAuthority;
        import org.springframework.security.core.userdetails.UserDetails;

        import com.boardend.boardend.models.Rider;
        import com.fasterxml.jackson.annotation.JsonIgnore;

public class RiderDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String email;

    private String username;

    @JsonIgnore
    private String password;

    private String streetAddress;

    private String vehicleNumber;

    private String companyState;

    private Collection<? extends GrantedAuthority> authorities;

    public RiderDetailsImpl(Long id, String name, String email, String username, String password, String streetAddress, String vehicleNumber, String companyState,
                            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.streetAddress = streetAddress;
        this.vehicleNumber = vehicleNumber;
        this.companyState = companyState;
        this.authorities = authorities;

    }

    public static RiderDetailsImpl build(Rider rider) {
        List<GrantedAuthority> authorities = rider.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new RiderDetailsImpl(
                rider.getId(),
                rider.getName(),
                rider.getEmail(),
                rider.getUsername(),
                rider.getPassword(),
                rider.getStreetAddress(),
                rider.getVehicleNumber(),
                rider.getCompanyState(),
                authorities);
    }

    // Add this constructor that accepts a Rider object
    public RiderDetailsImpl(Rider rider) {
        this.id = rider.getId();
        this.name = rider.getName();
        this.email = rider.getEmail();
        this.username = rider.getUsername();
        this.password = rider.getPassword();
        this.streetAddress = rider.getStreetAddress();
        this.vehicleNumber = rider.getVehicleNumber();
        this.companyState = rider.getCompanyState();
        this.authorities = rider.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        RiderDetailsImpl rider = (RiderDetailsImpl) o;
        return Objects.equals(id, rider.id);
    }
}

