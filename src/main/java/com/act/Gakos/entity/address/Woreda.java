package com.act.Gakos.entity.address;


import jakarta.persistence.*;

@Entity(name = "woreda")
@Table

public class Woreda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String zoneName;


    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneName() {
        return zone != null ? zone.getName() : null; // Check for null to avoid NullPointerException
    }

    @Override
    public String toString() {
        return "Woreda{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", zone=" + zone +
                ", zoneName=" + zoneName +
                '}';
    }
}
