package com.act.Gakos.entity.address;

import jakarta.persistence.*;

@Entity(name = "kebele")
@Table
public class Kebele {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
//    private String descriptions;
    @ManyToOne
    @JoinColumn(name = "woreda_id")
    private Woreda woreda;

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

//    public String getDescriptions() {
//        return descriptions;
//    }
//
//    public void setDescriptions(String descriptions) {
//        this.descriptions = descriptions;
//    }

    public Woreda getWoreda() {
        return woreda;
    }

    public void setWoreda(Woreda woreda) {
        this.woreda = woreda;
    }

    @Override
    public String toString() {
        return "Kebele{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", descriptions='" + descriptions + '\'' +
                ", woreda=" + woreda +
                '}';
    }

//    public String getDescription() {
//        return descriptions;
//    }
}
