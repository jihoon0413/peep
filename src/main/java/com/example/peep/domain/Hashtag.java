package com.example.peep.domain;

import com.example.peep.domain.enumType.HashtagType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column private String content;

    @Enumerated(value = EnumType.STRING)
    @Setter @Column private HashtagType type;

    private Hashtag(String content, HashtagType type) {
        this.content = content;
        this.type = type;
    }

    public static Hashtag of(String content, HashtagType type) {
        return new Hashtag(content, type);
    }

}
