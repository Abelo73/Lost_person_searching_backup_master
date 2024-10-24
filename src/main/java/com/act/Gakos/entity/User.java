package com.act.Gakos.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class User implements org.springframework.security.core.userdetails.UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private String password;

    private String resetToken;
    private LocalDateTime tokenExpiration;

    private String token;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updatedAt;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore // Prevent serialization issues
    private UserDetails userDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "father_details_id")
    private FatherDetails fatherDetails;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mother_details_id")
    private MotherDetails matherDetails;



    // One-to-One relationship with ProfileInfo
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_info_id", referencedColumnName = "id")
    private ProfileInfo profileInfo;

    // One-to-One relationship with AddressDetails
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_details_id", referencedColumnName = "id")
    private AddressDetails addressDetails;




    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id")
    private ProfilePicture profilePicture;


    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime lastSeen;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert role to authorities
        return Collections.singletonList(role::toString); // Assuming role is an enum that represents authorities
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getMiddleName(), user.getMiddleName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPhoneNumber(), user.getPhoneNumber()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getUserDetails(), user.getUserDetails()) && Objects.equals(getProfileInfo(), user.getProfileInfo()) && Objects.equals(getAddressDetails(), user.getAddressDetails()) && getRole() == user.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getMiddleName(), getLastName(), getEmail(), getUsername(), getPhoneNumber(), getPassword(), getUserDetails(), getProfileInfo(), getAddressDetails(), getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", tokenExpiration=" + tokenExpiration +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userDetails=" + userDetails +
                ", fatherDetails=" + fatherDetails +
                ", matherDetails=" + matherDetails +
                ", profileInfo=" + profileInfo +
                ", addressDetails=" + addressDetails +
                ", profilePicture=" + profilePicture +
                ", role=" + role +
                ", lastSeen=" + lastSeen +
                '}';
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
        return true; // You might want to implement this logic based on your needs
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You might want to implement this logic based on your needs
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You might want to implement this logic based on your needs
    }

    @Override
    public boolean isEnabled() {
        return true; // You might want to implement this logic based on your needs
    }
}
