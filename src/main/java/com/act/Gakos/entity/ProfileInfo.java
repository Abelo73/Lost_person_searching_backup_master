package com.act.Gakos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "profile_info")
public class ProfileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Storing metadata for personal images
//    @ElementCollection
//    @CollectionTable(name = "personal_images", joinColumns = @JoinColumn(name = "profile_id"))

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private List<ImageMetadataNew> personalImages;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private List<ImageMetadataNew> profilePicture;

    // Storing metadata for additional images
    @ElementCollection
    @CollectionTable(name = "additional_images", joinColumns = @JoinColumn(name = "profile_id"))
    private List<ImageMetadata> additionalImages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ImageMetadataNew> getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(List<ImageMetadataNew> profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<ImageMetadataNew> getPersonalImages() {
        return personalImages;
    }

    public void setPersonalImages(List<ImageMetadataNew> personalImages) {
        this.personalImages = personalImages;
    }

    public List<ImageMetadata> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(List<ImageMetadata> additionalImages) {
        this.additionalImages = additionalImages;
    }
}
