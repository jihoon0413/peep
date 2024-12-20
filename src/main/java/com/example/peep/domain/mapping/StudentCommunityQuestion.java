package com.example.peep.domain.mapping;

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
    private Student writer;

    @ManyToOne
    @JoinColumn(name = "chosen_id")
    private Student chosen;

    @ManyToOne
    @JoinColumn(name = "community_question_id")
    private CommunityQuestion communityQuestion;

    private StudentCommunityQuestion(Student writer, Student chosen, CommunityQuestion communityQuestion) {
        this.writer = writer;
        this.chosen = chosen;
        this.communityQuestion = communityQuestion;
    }

    public static StudentCommunityQuestion of(Student writer, Student chosen, CommunityQuestion communityQuestion) {
        return new StudentCommunityQuestion(writer, chosen, communityQuestion);
    }

}
