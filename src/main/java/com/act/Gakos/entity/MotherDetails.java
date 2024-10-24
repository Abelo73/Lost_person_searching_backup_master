package com.act.Gakos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mother_details")
public class MotherDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motherName;
    private String motherPhoneNumber;
    private String currentAddress;
    private Double age;
    private String bodyColor;
    private String height;
    private String occupations;


    @Lob
    private byte[] motherImage;

    public MotherDetails(String motherName, String bodyColor, byte[] motherImage, Long id, String height, String occupations, String currentAddress, String motherPhoneNumber) {
    }

    public MotherDetails() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherPhoneNumber() {
        return motherPhoneNumber;
    }

    public void setMotherPhoneNumber(String motherPhoneNumber) {
        this.motherPhoneNumber = motherPhoneNumber;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public String getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getOccupations() {
        return occupations;
    }

    public void setOccupations(String occupations) {
        this.occupations = occupations;
    }

    public byte[] getMotherImage() {
        return motherImage;
    }

    public void setMotherImage(byte[] motherImage) {
        this.motherImage = motherImage;
    }
}