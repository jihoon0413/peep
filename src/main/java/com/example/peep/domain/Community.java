package com.example.peep.domain;

import com.example.peep.domain.mapping_table.CommunityQuestion;
import com.example.peep.domain.mapping_table.StudentCommunity;
import jakarta.persistence.*;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Community extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private Set<StudentCommunity> students = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private Set<CommunityQuestion> questions = new HashSet<>();

    @Setter @Column private int grade;
    @Setter @Column private int myClass;

}
