package com.example.peep.repository;

import com.example.peep.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByTokenAndDeviceId(String token, String deviceId);
    RefreshToken findByUserIdAndDeviceId(String userId, String deviceId);
    void deleteByUserIdAndDeviceId(String userId, String deviceId);
    boolean findByToken(String token);
}
