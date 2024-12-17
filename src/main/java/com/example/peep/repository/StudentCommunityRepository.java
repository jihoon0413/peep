package com.example.peep.repository;

import com.example.peep.domain.Student;
import com.example.peep.domain.mapping.StudentCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentCommunityRepository extends JpaRepository<StudentCommunity, Long> {
    List<StudentCommunity> findAllByStudentUserId(String userId);
}
