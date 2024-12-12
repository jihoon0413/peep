package com.example.peep.domain.mapping;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Community;
import com.example.peep.domain.Student;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class StudentCommunity extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    private StudentCommunity(Student student, Community community) {
        this.student = student;
        this.community = community;
    }

    public static StudentCommunity of(Student student, Community community) {
        return new StudentCommunity(student, community);
    }


}
