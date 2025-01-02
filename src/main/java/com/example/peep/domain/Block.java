package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "blocker"),
        @Index(columnList = "blocked")
})
public class Block extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocker_id")
    private Student blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_id")
    private Student blocked;


    private Block(Student blocker, Student blocked) {
        this.blocker = blocker;
        this.blocked = blocked;
    }

    public static Block of(Student blocker, Student blocked) {
        return new Block(blocker, blocked);
    }

}
