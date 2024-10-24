package com.act.Gakos.dto;


import com.act.Gakos.entity.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;

    private String resetToken;
    private LocalDateTime tokenExpiration;

    private String token;

    private LocalDateTime createdAt;



    private LocalDateTime updatedAt;



    private UserDetails userDetails;


    private FatherDetails fatherDetails;

    // One-to-One relationship with ProfileInfo

    private ProfileInfo profileInfo;

    // One-to-One relationship with AddressDetails

    private AddressDetails addressDetails;

    private MotherDetails motherDetails;




    private Role role;

    public UserDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(LocalDateTime tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public FatherDetails getFatherDetails() {
        return fatherDetails;
    }

    public void setFatherDetails(FatherDetails fatherDetails) {
        this.fatherDetails = fatherDetails;
    }

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public MotherDetails getMotherDetails() {
        return motherDetails;
    }



    public void setMotherDetails(MotherDetails motherDetails) {
        this.motherDetails = motherDetails;
    }

    public UserDTO(Integer id, String firstName, String middleName, String lastName, String email, String username, String phoneNumber, String resetToken, LocalDateTime tokenExpiration, String token, LocalDateTime createdAt, LocalDateTime updatedAt, UserDetails userDetails, FatherDetails fatherDetails, ProfileInfo profileInfo, AddressDetails addressDetails,MotherDetails motherDetails ,Role role) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.resetToken = resetToken;
        this.tokenExpiration = tokenExpiration;
        this.token = token;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userDetails = userDetails;
        this.fatherDetails = fatherDetails;
        this.profileInfo = profileInfo;
        this.addressDetails = addressDetails;
        this.motherDetails = motherDetails;
        this.role = role;
    }
}
