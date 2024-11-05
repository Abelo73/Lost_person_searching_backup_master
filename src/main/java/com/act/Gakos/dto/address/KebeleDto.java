package com.act.Gakos.dto.address;

public class KebeleDto {

    private Integer id;
    private String name;
    private String woredaName;

    public KebeleDto(Integer id, String name, String woredaName) {
        this.id = id;
        this.name = name;
        this.woredaName = woredaName;
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

    @Override
    public String toString() {
        return "KebeleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", woredaName='" + woredaName + '\'' +

                '}';
    }
}
