package com.example.peep.repository;

import com.example.peep.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserIdAndDeviceId(String userId, String deviceId);
    void deleteByUserIdAndToken(String userId, String refreshToken);

}
