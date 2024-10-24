package com.act.Gakos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String imageName;
    private LocalDateTime registeredDate;
    private String description;
    private LocalDateTime dateOfPictureTaken;
    private String locationOfPictureTaken;
    private Long size;
    private String type;

    private String imagePath;
    @Lob // This annotation is used to indicate a large object (LOB)
    private byte[] imageData; // Store image as byte array

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "profile_picture_id", nullable = false)
//    private ProfilePicture profilePicture;


//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_picture_id")
    private ProfilePicture profilePicture;

}
