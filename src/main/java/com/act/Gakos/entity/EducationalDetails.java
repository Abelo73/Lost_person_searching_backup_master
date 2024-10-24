package com.act.Gakos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "educational_details")
public class EducationalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String primarySchoolName;
    private String highSchoolName;
    private String collegeName;
    private String rank;


}
