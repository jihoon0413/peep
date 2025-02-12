package com.example.peep.repository;

import com.example.peep.domain.mapping.StudentCommunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCommunityRepository extends JpaRepository<StudentCommunity, Long> {
    List<StudentCommunity> findAllByStudentUserId(String userId);
}
