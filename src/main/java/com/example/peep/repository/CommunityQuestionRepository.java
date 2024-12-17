package com.example.peep.repository;

import com.example.peep.domain.mapping.CommunityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityQuestionRepository extends JpaRepository<CommunityQuestion, Long> {
    List<CommunityQuestion> findAllByCommunityId(Long id);
}
