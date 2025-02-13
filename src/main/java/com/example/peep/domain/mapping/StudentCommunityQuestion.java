package com.example.peep.domain.mapping;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StudentCommunityQuestion extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Student writerInCommunity;

    @ManyToOne
    @JoinColumn(name = "chosen_id")
    private Student chosenInCommunity;

    @ManyToOne
    @JoinColumn(name = "community_question_id")
    private CommunityQuestion communityQuestion;

    private StudentCommunityQuestion(Student writer, Student chosen, CommunityQuestion communityQuestion) {
        this.writerInCommunity = writer;
        this.chosenInCommunity = chosen;
        this.communityQuestion = communityQuestion;
    }

    public static StudentCommunityQuestion of(Student writer, Student chosen, CommunityQuestion communityQuestion) {
        return new StudentCommunityQuestion(writer, chosen, communityQuestion);
    }

}
