package com.act.Gakos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "brother_details")
public class BrotherDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brotherName;

    @Lob
    private byte[] brotherImage;

    // Other relevant details for the brother
    // Getters and Setters
    // ...
}