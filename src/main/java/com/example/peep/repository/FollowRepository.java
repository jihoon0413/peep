package com.example.peep.repository;

import com.example.peep.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerUserIdAndFollowingUserId(String follower, String following);
    void deleteByFollowerUserIdAndFollowingUserId(String follower, String following);
    List<Follow> findAllByFollowerUserId(String userId);
    List<Follow> findAllByFollowingUserId(String userId);
}
