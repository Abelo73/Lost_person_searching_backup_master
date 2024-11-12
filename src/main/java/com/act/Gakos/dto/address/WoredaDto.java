package com.act.Gakos.dto.address;

public class WoredaDto {
    private Integer id;
    private String name;
    private String description;
    private String zoneName;
private Long zoneId;
    public WoredaDto(Integer id, String name, String description, Long zoneId, String  zoneName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
    }

    public WoredaDto() {

    }


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


    public String getZoneName() {
        return zoneName;
    }

    public Object setZoneName(String zoneName) {
        this.zoneName = zoneName;
        return null;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return "WoredaDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", zoneName='" + zoneName + '\'' +

                '}';
    }


    public Long getZoneId() {
        return zoneId;
    }
}
