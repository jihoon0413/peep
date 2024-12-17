package com.example.peep.repository;

import com.example.peep.domain.mapping.StudentCommunityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCommunityQuestionRepository extends JpaRepository<StudentCommunityQuestion, Long> {
}
