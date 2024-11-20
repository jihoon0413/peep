package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class Coin extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private int myCoin;

    private Coin(int myCoin) {
        this.myCoin = myCoin;
    }

    public static Coin of(int myCoin) {
        return new Coin(myCoin);
    }
}
