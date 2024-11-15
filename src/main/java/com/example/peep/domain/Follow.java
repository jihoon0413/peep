package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Setter;

@Entity
public class Follow extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "follower_id")
    private Student follower_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "following_id")
    private Student following_id;
}
