package com.act.Gakos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name = "user_details")

public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String growthPlace;

    // One-to-One relationship with User
//    @OneToOne(mappedBy = "user_details")
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public UserDetails() {

    }


    @Override
    public String toString() {
        return "UserDetails{" +
                "id=" + id +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", growthPlace='" + growthPlace + '\'' +
                ", user=" + user +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getGrowthPlace() {
        return growthPlace;
    }

    public void setGrowthPlace(String growthPlace) {
        this.growthPlace = growthPlace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetails(Long id, Integer age, String phoneNumber, LocalDate dateOfBirth, String placeOfBirth, String growthPlace, User user) {
        this.id = id;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.growthPlace = growthPlace;
        this.user = user;
    }
}
