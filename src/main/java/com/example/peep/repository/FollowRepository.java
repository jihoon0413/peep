package com.example.peep.repository;

import com.example.peep.domain.Follow;
import com.example.peep.dto.response.StudentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerUserIdAndFollowingUserId(String follower, String following);
    Optional<Follow> deleteByFollowerUserIdAndFollowingUserId(String follower, String following);
    List<Follow> findAllByFollowerUserId(String userId, Pageable pageable);
    List<Follow> findAllByFollowingUserId(String userId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM follow WHERE following_id = :id", nativeQuery = true)
    Integer countByFollowingUserId(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM follow WHERE follower_id = :id", nativeQuery = true)
    Integer countByFollowerUserId(@Param("id") Long id);


    @Modifying
    @Query(value = "UPDATE follow f SET f.is_deleted = true WHERE f.follower_id = :id OR f.following_id = :id", nativeQuery = true)
    void softDeleteAllByUserId(@Param("id") Long id);
}
