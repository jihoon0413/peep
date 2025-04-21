package com.example.peep.repository;

import com.example.peep.domain.Student;
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
    List<Student> findRandomFourStudents(Long communityId, String currentUserId);
}
