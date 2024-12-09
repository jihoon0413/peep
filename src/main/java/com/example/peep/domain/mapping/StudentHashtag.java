package com.example.peep.domain.mapping;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Hashtag;
import com.example.peep.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
public class StudentHashtag extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    private StudentHashtag(Student student, Hashtag hashtag) {
        this.student = student;
        this.hashtag = hashtag;
    }

    public static StudentHashtag of(Student student, Hashtag hashtag) {
        return new StudentHashtag(student, hashtag);
    }

}
