package com.act.Gakos.entity.address;


import jakarta.persistence.*;

@Entity(name = "got")
@Table
public class Got {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String nickname;
    private String description;

    @ManyToOne
    @JoinColumn(name = "kebele_id")
    private Kebele kebele;


    public Got(Integer id, String name, String nickname, String description, Kebele kebele) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.description = description;
        this.kebele = kebele;
    }

    public Got() {

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

    public Kebele getKebele() {
        return kebele;
    }

    public void setKebele(Kebele kebele) {
        this.kebele = kebele;
    }

    @Override
    public String toString() {
        return "Got{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", description='" + description + '\'' +
                ", kebele=" + kebele +
                '}';
    }
}
