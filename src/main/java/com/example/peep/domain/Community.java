package com.example.peep.domain;

import com.example.peep.domain.mapping.CommunityQuestion;
import com.example.peep.domain.mapping.StudentCommunity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Community extends AuditingFields{

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private Set<StudentCommunity> students = new HashSet<>();

    @Setter @Column private int grade;
    @Setter @Column private int myClass;

    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private Set<CommunityQuestion> questions = new HashSet<>();

    private Community(School school, int grade, int myClass) {
        this.school = school;
        this.grade = grade;
        this.myClass = myClass;
    }

    private Community(School school) {
        this.school = school;
        this.grade = 0;
        this.myClass = 0;

    }

    public static Community of(School school, int grade, int myClass) {
        return new Community(school, grade, myClass);
    }

    public static Community of(School school) {
        return new Community(school);
    }
}
