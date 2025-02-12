package com.example.peep.repository;

import com.example.peep.domain.mapping.CommunityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CommunityQuestionRepository extends JpaRepository<CommunityQuestion, Long> {
    List<CommunityQuestion> findAllByCommunityId(Long id);

    @Query(value = "SELECT * FROM community_question WHERE community_id = :id AND DATE(created_at) = :date", nativeQuery = true)
    List<CommunityQuestion> findAllByCommunityIdAndDate(@Param("id") Long id, @Param("date") LocalDate date);
}
