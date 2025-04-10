package com.example.peep.service;

import com.example.peep.domain.Community;
import com.example.peep.domain.Question;
import com.example.peep.domain.Student;
import com.example.peep.domain.enumType.QuestionType;
import com.example.peep.domain.mapping.CommunityQuestion;
import com.example.peep.domain.mapping.StudentCommunity;
import com.example.peep.domain.mapping.StudentCommunityQuestion;
import com.example.peep.domain.mapping.StudentQuestion;
import com.example.peep.dto.CommunityQuestionDto;
import com.example.peep.dto.QuestionDto;
import com.example.peep.dto.StudentQuestionDto;
import com.example.peep.dto.response.ChosenQuestionResponse;
import com.example.peep.dto.response.HomeQuestionListResponse;
import com.example.peep.errors.errorcode.CustomErrorCode;
import com.example.peep.errors.exception.RestApiException;
import com.example.peep.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final CommunityRepository communityRepository;
    private final StudentCommunityRepository studentCommunityRepository;
    private final CommunityQuestionRepository communityQuestionRepository;
    private final StudentCommunityQuestionRepository studentCommunityQuestionRepository;
    private final StudentQuestionRepository studentQuestionRepository;

    public ResponseEntity<Void> commonChooseStudent(String myId, String userId, Long communityQuestionId) {

        Student writer = studentRepository.findByUserId(myId).orElseThrow();

        Student chosen = studentRepository.findByUserId(userId).orElseThrow();

        CommunityQuestion communityQuestion = communityQuestionRepository.findById(communityQuestionId).orElseThrow();

        StudentCommunityQuestion studentCommunityQuestion = StudentCommunityQuestion.of(writer, chosen, communityQuestion);

        studentCommunityQuestionRepository.save(studentCommunityQuestion);

        return ResponseEntity.noContent().build();

    }

    public ResponseEntity<Void> randomChooseStudent(String myId, String userId, Long studentQuestionId) {

        Student chosen = studentRepository.findByUserId(userId).orElseThrow();

        StudentQuestion studentQuestion = studentQuestionRepository.findById(studentQuestionId).orElseThrow();

        if(!Objects.equals(studentQuestion.getWriter().getUserId(), myId)) {
            throw new RestApiException(CustomErrorCode.WRONG_VERIFICATION_CODE);
        }
        studentQuestion.setChosen(chosen);

        studentQuestionRepository.save(studentQuestion);

        return ResponseEntity.noContent().build();
    }


    public ResponseEntity<HomeQuestionListResponse> getQuestionList(String myId) {


        List<StudentCommunity> studentCommunities = studentCommunityRepository.findAllByStudentUserId(myId);

        List<CommunityQuestionDto> communityQuestionDtoList = new ArrayList<>();
        List<StudentQuestionDto> studentQuestionList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        Long id = studentCommunities.getFirst().getStudent().getId();
        for(StudentCommunity studentCommunity : studentCommunities) {
            communityQuestionRepository.findAllByCommunityIdAndDate(studentCommunity.getCommunity().getId(), date)
                    .forEach(communityQuestion -> {
                        communityQuestionDtoList.add(CommunityQuestionDto.from(communityQuestion));
                    });
        }

        studentQuestionRepository.findAllByWriterIdAndDate(id, date)
                .forEach(studentQuestion -> {
                    studentQuestionList.add(StudentQuestionDto.from(studentQuestion));
                });

        HomeQuestionListResponse homeResponse = HomeQuestionListResponse.of(communityQuestionDtoList, studentQuestionList);

        return ResponseEntity.ok(homeResponse);
    }

    public ResponseEntity<List<ChosenQuestionResponse>> getChosenQuestionList(String myId) {

        List<ChosenQuestionResponse> questionResponseList = new ArrayList<>();

        studentCommunityQuestionRepository.findAllByChosenInCommunityUserId(myId)
                .forEach(studentCommunityQuestion -> {
                    questionResponseList.add(ChosenQuestionResponse.of(studentCommunityQuestion.getId(),
                            QuestionDto.from(studentCommunityQuestion.getCommunityQuestion().getQuestion()),
                            studentCommunityQuestion.getUpdatedAt(),
                            QuestionType.COMMON,
                            studentCommunityQuestion.getWriterInCommunity().getGender()
                    ));
                });

        studentQuestionRepository.findAllByChosenUserId(myId)
                .forEach(studentQuestion -> {
                    questionResponseList.add(ChosenQuestionResponse.of(studentQuestion.getId(),
                            QuestionDto.from(studentQuestion.getQuestion()),
                            studentQuestion.getUpdatedAt(),
                            QuestionType.RANDOM,
                            studentQuestion.getWriter().getGender()
                            ));
                });

        questionResponseList.sort(Comparator.comparing(ChosenQuestionResponse::chosenDate).reversed());

        return ResponseEntity.ok(questionResponseList);
    }
    //TODO: 대용량 처리가 예상됨으로 spring batch 도입 고려

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3, initialDelay = 1000 * 30)
    public void updateRandomCommunityQuestion() {
        List<Community> communities = communityRepository.findAll();
        List<CommunityQuestion> communityQuestions = new ArrayList<>();

        for(Community community : communities) {
            List<Question> questions = questionRepository.findRandomQuestions(1);
            for (Question question : questions) {
                CommunityQuestion communityQuestion = CommunityQuestion.of(community, question);
                communityQuestions.add(communityQuestion);
            }
        }
        communityQuestionRepository.saveAll(communityQuestions);
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3, initialDelay = 1000 * 30)
    public void updateRandomStudentQuestion() {
        List<Student> students = studentRepository.findAll();
        List<StudentQuestion> studentQuestions = new ArrayList<>();

        for(Student student : students) {
            List<Question> questions = questionRepository.findRandomQuestions(4);
            for (Question question : questions) {
                StudentQuestion studentQuestion = StudentQuestion.of(student, question);
                studentQuestions.add(studentQuestion);
            }
        }
        studentQuestionRepository.saveAll(studentQuestions);
    }


}
