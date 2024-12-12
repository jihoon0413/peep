package com.example.peep.repository;

import com.example.peep.domain.mapping.CommunityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityQuestionRepository extends JpaRepository<CommunityQuestion, Long> {
}
