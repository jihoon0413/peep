package com.example.peep.repository;

import com.example.peep.domain.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> deleteByBlockerUserIdAndBlockedUserId(String blocker, String blocked);
    Optional<Block> findByBlockerUserIdAndBlockedUserId(String userId, String myId);
}
