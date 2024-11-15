package com.example.peep.domain.mapping_table;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Hashtag;
import com.example.peep.domain.Student;
import jakarta.persistence.*;

@Entity
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


}
