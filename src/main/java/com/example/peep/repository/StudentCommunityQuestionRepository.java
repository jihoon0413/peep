package com.example.peep.repository;

import com.example.peep.domain.mapping.StudentCommunityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCommunityQuestionRepository extends JpaRepository<StudentCommunityQuestion, Long> {
    List<StudentCommunityQuestion>findAllByChosenInCommunityUserId(String myId);
}
