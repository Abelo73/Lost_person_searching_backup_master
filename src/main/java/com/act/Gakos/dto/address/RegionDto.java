package com.act.Gakos.dto.address;

import com.act.Gakos.entity.address.Country;

public class RegionDto {
    private Long id;

    private String name;
    private String description;
    private Integer countryId;

    public RegionDto() {
    }

    public RegionDto(Long id, String name, String description, Integer countryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.countryId = countryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "RegionDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
