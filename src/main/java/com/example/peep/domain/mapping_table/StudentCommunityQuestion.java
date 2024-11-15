package com.example.peep.domain.mapping_table;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Student;
import jakarta.persistence.*;

@Entity
public class StudentCommunityQuestion extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Student writer_id;

    @ManyToOne
    @JoinColumn(name = "chosen_id")
    private Student chosen_id;

    @ManyToOne
    @JoinColumn(name = "community_question_id")
    private CommunityQuestion communityQuestion;

}
