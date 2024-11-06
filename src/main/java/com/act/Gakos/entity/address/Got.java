package com.act.Gakos.entity.address;

import com.act.Gakos.entity.address.Kebele;
import jakarta.persistence.*;

@Entity(name = "got")
@Table(name = "got")
public class Got {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String nickname;
    private String description;

    @ManyToOne
    @JoinColumn(name = "kebele_id", referencedColumnName = "id") // Mapping kebele as a ManyToOne relationship
    private Kebele kebele;

    public Got(Integer id, String name, String nickname, String description, Kebele kebele) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.description = description;
        this.kebele = kebele;
    }

    public Got() {}

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

    // Getter method to retrieve kebele_id directly, without mapping it as a separate field
    public Integer getKebeleId() {
        return kebele != null ? kebele.getId() : null;
    }

    @Override
    public String toString() {
        return "Got{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", description='" + description + '\'' +
                ", kebele=" + (kebele != null ? kebele.getId() : null) +
                '}';
    }
}
