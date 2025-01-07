package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "follower_id"),
        @Index(columnList = "following_id"),
})
@SQLRestriction("is_deleted = false")
@Getter
@Entity
public class Follow extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Student follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private Student following;

    private Follow(Student follower, Student following){
        this.follower = follower;
        this.following = following;
    }

    public static Follow of(Student follower, Student following) {
        return new Follow(follower, following);
    }

}
