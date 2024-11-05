package com.act.Gakos.dto.address;

public class ZoneDto {
    private Long id;
    private String name;
    private String description;
    private String regionName;


    public ZoneDto(Long id, String name, String description, String regionName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.regionName = regionName;
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

    public String getRegionName() {
        return regionName; // Getter for the region name
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName; // Setter for the region name
    }

    @Override
    public String toString() {
        return "ZoneDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", regionName='" + regionName + '\'' +
                '}';
    }
}
