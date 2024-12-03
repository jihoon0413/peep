package com.example.peep.domain;

import com.example.peep.dto.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class LoginRecord extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column @Setter private String userId;
    @Column @Setter private String clientIp;
    @Column @Setter private String userAgent;

    @Enumerated(value = EnumType.STRING)
    @Column @Setter private Event eventType;

    private LoginRecord(String userId, String clientIp, String userAgent, Event eventType) {
        this.userId = userId;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
        this.eventType = eventType;
    }

    public static LoginRecord of(String userId, String clientIp, String userAgent, Event eventType) {
        return new LoginRecord(userId, clientIp, userAgent, eventType);
    }

}
