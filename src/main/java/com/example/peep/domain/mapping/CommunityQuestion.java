package com.example.peep.domain.mapping;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Community;
import com.example.peep.domain.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

    private CommunityQuestion(Community community, Question question) {
        this.community = community;
        this.question = question;
    }

    public static CommunityQuestion of(Community community, Question question) {
        return new CommunityQuestion(community, question);
    }

}
