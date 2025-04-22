package com.example.peep.repository;

import com.example.peep.domain.Student;
import com.example.peep.dto.response.StudentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(String userId);

    @Query(value = """
        SELECT *\s
        FROM student s
        WHERE s.id IN (
            SELECT sc.student_id\s
            FROM student_community sc
            WHERE sc.community_id = ?
        )\s
        AND s.user_id != ?
        ORDER BY RAND()
        LIMIT 4
    """, nativeQuery = true)
    List<Student> findFourStudentsInCommunity(Long communityId, String currentUserId);

    @Query(value = """
        SELECT *
        FROM student s
        WHERE s.id IN (
            SELECT f.following_id
            FROM follow f
            WHERE f.follower_id = ?
        )
        ORDER BY RAND()
        LIMIT 4
    """, nativeQuery = true)
    List<Student> findFourStudentsInMyFollowing(Long id);

}
