package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
public class School extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column private String educationOffice;
    @Setter @Column private String schoolName;
}
