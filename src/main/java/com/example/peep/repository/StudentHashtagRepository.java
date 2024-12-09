package com.example.peep.repository;

import com.example.peep.domain.mapping.StudentHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentHashtagRepository extends JpaRepository<StudentHashtag, Long> {
    Optional<StudentHashtag> findByStudentUserIdAndHashtagId(String userId, Long id);
    void deleteByStudentUserIdAndHashtagId(String userId, Long id);
    List<StudentHashtag> findAllByStudentUserIdOrderByHashtagTypeAscHashtagContentAsc(String userId);
}
