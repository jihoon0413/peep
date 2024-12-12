package com.example.peep.repository;

import com.example.peep.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM question WHERE id >= FLOOR(RAND() * (SELECT MAX(id) FROM question)) LIMIT 1", nativeQuery = true)
    List<Question> findRandomQuestions();
}
