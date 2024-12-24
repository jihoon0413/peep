package com.example.peep.repository;

import com.example.peep.domain.Hashtag;
import com.example.peep.domain.enumType.HashtagType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findAllByType(HashtagType type);
}
