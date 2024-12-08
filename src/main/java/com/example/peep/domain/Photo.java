package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
@Entity
public class Photo extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private String photoUrl;

    private Photo(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public static Photo of(String photoUrl) {
        return new Photo(photoUrl);
    }
}
