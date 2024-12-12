package com.example.peep.repository;

import com.example.peep.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findBySchoolIdAndGradeAndMyClass(Long schoolId, int grade, int myClass);
}
