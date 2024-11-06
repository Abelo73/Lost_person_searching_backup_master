package com.act.Gakos.dto.address;

public class KebeleDto {

    private Integer id;
    private String name;
    private String woredaName;
    private String zoneName;
    private String regionName;
    private String countryName;

    public KebeleDto(Integer id, String name, String woredaName, String zoneName, String regionName, String countryName) {
        this.id = id;
        this.name = name;
        this.woredaName = woredaName;
        this.zoneName = zoneName;
        this.regionName = regionName;
        this.countryName = countryName;
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


    public String getWoredaName() {
        return woredaName;
    }

    public void setWoredaName(String woredaName) {
        this.woredaName = woredaName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return "KebeleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", woredaName='" + woredaName + '\'' +
                ", zoneName='" + zoneName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", countryName='" + countryName + '\'' +


                '}';
    }
}
