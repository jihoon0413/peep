package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 식별자

    @Setter @Column private String userId; // 사용자 ID
    @Setter @Column private String deviceId; // 디바이스 또는 세션 ID
    @Setter @Column private String token; // Refresh Token 값
    @Setter @Column private Date expiryDate; // 만료 시간

    private RefreshToken(String userId, String deviceId, String token, Date expiryDate) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public static RefreshToken of(String userId, String deviceId, String token, Date expiryDate) {
        return new RefreshToken(userId, deviceId, token, expiryDate);
    }

}
