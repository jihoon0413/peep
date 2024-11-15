package com.example.peep.domain;

import com.example.peep.domain.mapping_table.StudentCommunity;
import com.example.peep.domain.mapping_table.StudentCommunityQuestion;
import com.example.peep.domain.mapping_table.StudentHashtag;
import jakarta.persistence.*;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Table(indexes = {
        @Index(columnList = "userId")
})
@Entity
public class Student extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false) private String userId;
    @Setter @Column private String userPassword;
    @Setter @Column private String name;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @OneToOne
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ToString.Exclude
    @OneToMany(mappedBy = "student")
    private Set<StudentCommunity> communities = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "student")
    private Set<StudentHashtag> hashtags = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "following_id")
    private Set<Follow> followers;

    @ToString.Exclude
    @OneToMany(mappedBy = "follower_id")
    private Set<Follow> following;

    @ToString.Exclude
    @OneToMany(mappedBy = "writer_id")
    private Set<StudentCommunityQuestion> writer;

    @ToString.Exclude
    @OneToMany(mappedBy = "chosen_id")
    private Set<StudentCommunityQuestion> chosen;

    @Setter @Column private String tel;
    @Setter @Column private int grade;
    @Setter @Column private int myClass;



}
