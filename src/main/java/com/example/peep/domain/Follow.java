package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "follower"),
        @Index(columnList = "following"),
})
@SQLDelete(sql = "UPDATE follow SET is_deleted = true WHERE id = ?")
@Getter
@Entity
public class Follow extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "follower_id")
    private Student follower;

    @ManyToOne(optional = false)
    @JoinColumn(name = "following_id")
    private Student following;

    private Follow(Student follower, Student following){
        this.follower = follower;
        this.following = following;
    }

    public static Follow of(Student follower, Student following) {
        return new Follow(follower, following);
    }

}
