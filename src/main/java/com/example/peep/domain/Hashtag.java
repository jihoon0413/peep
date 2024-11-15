package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column private String content;
}
