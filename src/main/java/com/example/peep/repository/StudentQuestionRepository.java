package com.example.peep.repository;

import com.example.peep.domain.mapping.StudentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StudentQuestionRepository extends JpaRepository<StudentQuestion, Long> {
    @Query(value = "SELECT * FROM student_question WHERE writer_id = :id AND DATE(created_at) = :date", nativeQuery = true)
    List<StudentQuestion> findAllByWriterIdAndDate(@Param("id") Long id, @Param("date") LocalDate date);
}
