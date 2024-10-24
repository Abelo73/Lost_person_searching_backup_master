package com.act.Gakos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imageData")
@Builder
public class UploadImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    private String path;

    @Lob
    @Column(name="imagedata", length = 1000)
    private byte[] imageData;

    public UploadImage(String name, String path, byte[] imageData) {
        this.name = name;
        this.path = path;
        this.imageData = imageData;
    }
}
