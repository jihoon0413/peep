package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
@Table(indexes = {
        @Index(columnList = "userId")
})
@Entity
public class Student extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(name = "user_id", nullable = false, unique = true) private String userId;
    @Setter @Column @ToString.Exclude private String userPassword;
    @Setter @Column private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @Setter
    @JoinColumn(name = "school_id")
    private School school;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coin_id", referencedColumnName = "id")
    private Coin coin;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private Photo photo;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "student")
//    private Set<StudentCommunity> communities = new HashSet<>();
//
//    @ToString.Exclude
//    @OneToMany(mappedBy = "student")
//    private Set<StudentHashtag> hashtags = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Follow> followers;

    @ToString.Exclude
    @OneToMany(mappedBy = "following", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Follow> following;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "writer_id")
//    private Set<StudentCommunityQuestion> writer;
//
//    @ToString.Exclude
//    @OneToMany(mappedBy = "chosen_id")
//    private Set<StudentCommunityQuestion> chosen;

    @Setter @Column private int grade;
    @Setter @Column private int myClass;
    @Setter @Column private String tel;


    private Student (String userId, String userPassword, String name, School school, Coin coin, Photo photo, int grade, int myClass , String tel) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.name = name;
        this.school = school;
        this.coin = coin;
        this.photo = photo;
        this.grade = grade;
        this.myClass = myClass;
        this.tel = tel;
    }

    public static Student of(String userId, String userPassword, String name, School school, Coin coin, Photo photo, int grade, int myClass, String tel) {
        return new Student(userId, userPassword, name, school, coin, photo, grade, myClass, tel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

