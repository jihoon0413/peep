package com.example.peep.domain.mapping_table;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Community;
import com.example.peep.domain.Question;
import jakarta.persistence.*;

@Entity
public class CommunityQuestion extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;


}
