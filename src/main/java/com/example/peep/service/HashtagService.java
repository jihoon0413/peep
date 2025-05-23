package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.Hashtag;
import com.example.peep.domain.Student;
import com.example.peep.domain.enumType.HashtagType;
import com.example.peep.domain.mapping.StudentHashtag;
import com.example.peep.dto.HashtagDto;
import com.example.peep.repository.HashtagRepository;
import com.example.peep.repository.StudentHashtagRepository;
import com.example.peep.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final StudentRepository studentRepository;
    private final StudentHashtagRepository studentHashtagRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public List<HashtagDto> getHashList(HashtagType type) {

        return hashtagRepository.findAllByType(type)
                .stream().map(HashtagDto::from)
                .toList();
    }

    public List<HashtagDto> getMyHashtag(String myId) {

        return studentHashtagRepository.findAllByStudentUserIdOrderByHashtagTypeAscHashtagContentAsc(myId)
                .stream().map(studentHashtag -> HashtagDto.from(studentHashtag.getHashtag()))
                .toList();
    }

    public String setMyHashtag(String myId, List<HashtagDto> hashtagDtoList) {

        for (HashtagDto hashtagDto : hashtagDtoList) {
            StudentHashtag studentHashtag = studentHashtagRepository.findByStudentUserIdAndHashtagId(myId, hashtagDto.id()).orElse(null);
            if (studentHashtag == null) {
                Student student = studentRepository.findByUserId(myId).orElseThrow();
                Hashtag hashtag = hashtagRepository.findById(hashtagDto.id()).orElseThrow();
                studentHashtagRepository.save(StudentHashtag.of(student, hashtag));
            }
        }
        return "Success set your hashtags";
    }

    public String deleteMyHashtag(String myId, List<HashtagDto> hashtagDtoList) {

        for (HashtagDto hashtagDto : hashtagDtoList) {
            studentHashtagRepository.deleteByStudentUserIdAndHashtagId(myId, hashtagDto.id());
        }
        return "Success delete your hashtag";
    }
}