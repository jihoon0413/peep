package com.example.peep.domain.mapping;

import com.example.peep.domain.AuditingFields;
import com.example.peep.domain.Question;
import com.example.peep.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudentQuestion extends AuditingFields {

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
    @JoinColumn(name = "question_id")
    private Question question;

    private boolean whether;

    private StudentQuestion(Student writer, Question question) {
        this.writer=writer;
        this.question = question;
        this.whether = false;
    }

    public static StudentQuestion of(Student writer, Question question) {
        return new StudentQuestion(writer, question);
    }


}
