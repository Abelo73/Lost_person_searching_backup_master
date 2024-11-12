package com.act.Gakos.entity.address;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity(name = "country")
@Table
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String countryName;
    private String descriptions;

    public Country() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Country(Integer id, String countryName, String descriptions) {
        this.id = id;
        this.countryName = countryName;
        this.descriptions = descriptions;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", countryName='" + countryName + '\'' +
                ", descriptions='" + descriptions + '\'' +
                '}';
    }


}
