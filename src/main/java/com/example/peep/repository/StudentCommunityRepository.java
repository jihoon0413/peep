package com.example.peep.repository;

import com.example.peep.domain.mapping.StudentCommunity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCommunityRepository extends JpaRepository<StudentCommunity, Long> {
}
