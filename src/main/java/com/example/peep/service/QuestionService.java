package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.Student;
import com.example.peep.domain.mapping.CommunityQuestion;
import com.example.peep.domain.mapping.StudentCommunity;
import com.example.peep.domain.mapping.StudentCommunityQuestion;
import com.example.peep.dto.CommunityQuestionDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final JwtTokenProvider jwtTokenProvider;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final StudentCommunityRepository studentCommunityRepository;
    private final CommunityQuestionRepository communityQuestionRepository;
    private final StudentCommunityQuestionRepository studentCommunityQuestionRepository;

    public ResponseEntity<Void> chooseStudent(String token, String userId, Long communityQuestionId) {
        String myId = jwtTokenProvider.getUserId(token);

        Student writer = studentRepository.findByUserId(myId).orElseThrow();

        Student chosen = studentRepository.findByUserId(userId).orElseThrow();

        CommunityQuestion communityQuestion = communityQuestionRepository.findById(communityQuestionId).orElseThrow();

        StudentCommunityQuestion studentCommunityQuestion = StudentCommunityQuestion.of(writer, chosen, communityQuestion);

        studentCommunityQuestionRepository.save(studentCommunityQuestion);

        return ResponseEntity.noContent().build();

    }

    public ResponseEntity<List<CommunityQuestionDto>> getQuestionList(String token) {

        String myId = jwtTokenProvider.getUserId(token);

        List<StudentCommunity> studentCommunities = studentCommunityRepository.findAllByStudentUserId(myId);

        List<CommunityQuestionDto> communityQuestionDtoList = new ArrayList<>();
        for(StudentCommunity studentCommunity : studentCommunities) {
            communityQuestionRepository.findAllByCommunityId(studentCommunity.getCommunity().getId())
                    .forEach(communityQuestion -> {
                        communityQuestionDtoList.add(CommunityQuestionDto.from(communityQuestion));
                    });
        }

        return ResponseEntity.ok(communityQuestionDtoList);
    }
}
