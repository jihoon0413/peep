package com.example.peep.domain.mapping_table;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Community;
import com.example.peep.domain.Student;
import jakarta.persistence.*;

@Entity
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




}
