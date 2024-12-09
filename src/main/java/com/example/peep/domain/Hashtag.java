package com.example.peep.domain;

import com.example.peep.enumType.HashtagType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column private String content;

    @Enumerated(value = EnumType.STRING)
    @Setter @Column private HashtagType type;
}
