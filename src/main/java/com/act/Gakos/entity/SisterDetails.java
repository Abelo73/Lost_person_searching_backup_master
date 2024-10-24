package com.act.Gakos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sister_details")
public class SisterDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sisterName;

    @Lob
    private byte[] sisterImage;


}