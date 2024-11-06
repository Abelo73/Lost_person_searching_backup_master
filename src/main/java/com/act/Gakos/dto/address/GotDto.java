package com.act.Gakos.dto.address;

public class GotDto {
    private Integer id;
    private String name;
    private String nickname;
    private String description;
    private String kebeleName;

    public GotDto(Integer id, String name, String nickname, String description, String kebeleName) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.description = description;
        this.kebeleName = kebeleName;
    }

    public GotDto() {

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKebeleName() {
        return kebeleName;
    }

    public void setKebeleName(String kebeleName) {
        this.kebeleName = kebeleName;
    }
}
