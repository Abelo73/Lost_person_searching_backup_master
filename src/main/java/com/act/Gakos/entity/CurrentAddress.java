package com.act.Gakos.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "current_address")
@Table
public class CurrentAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String country;
    private String region;
    private String zone;
    private String woreda;
    private String kebele;
    private String got;
    private LocalDate startedYear;
    private LocalDate endedYear;
    private Integer calculatedDuration;  // duration calculated based on start and end dates
    private Integer age;
    private String notes;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getWoreda() {
        return woreda;
    }

    public void setWoreda(String woreda) {
        this.woreda = woreda;
    }

    public String getKebele() {
        return kebele;
    }

    public void setKebele(String kebele) {
        this.kebele = kebele;
    }

    public String getGot() {
        return got;
    }

    public void setGot(String got) {
        this.got = got;
    }

    public LocalDate getStartedYear() {
        return startedYear;
    }

    public void setStartedYear(LocalDate startedYear) {
        this.startedYear = startedYear;
    }

    public LocalDate getEndedYear() {
        return endedYear;
    }

    public void setEndedYear(LocalDate endedYear) {
        this.endedYear = endedYear;
    }

    public Integer getCalculatedDuration() {
        return calculatedDuration;
    }

    public void setCalculatedDuration(Integer calculatedDuration) {
        this.calculatedDuration = calculatedDuration;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CurrentAddress{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", zone='" + zone + '\'' +
                ", woreda='" + woreda + '\'' +
                ", kebele='" + kebele + '\'' +
                ", got='" + got + '\'' +
                ", startedYear=" + startedYear +
                ", endedYear=" + endedYear +
                ", calculatedDuration=" + calculatedDuration +
                ", age=" + age +
                ", notes='" + notes + '\'' +
                ", user=" + user +
                '}';
    }
}
