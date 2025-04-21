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
import com.example.peep.errors.ErrorCode;
import com.example.peep.errors.PeepApiException;
import com.example.peep.repository.*;
import com.example.peep.details.HintUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final HintUtils hintUtils;

    public String commonChooseStudent(String myId, String userId, Long communityQuestionId) {

        Student writer = studentRepository.findByUserId(myId).orElseThrow();

        Student chosen = studentRepository.findByUserId(userId).orElseThrow(() -> new PeepApiException(ErrorCode.USER_NOT_FOUND));

        CommunityQuestion communityQuestion = communityQuestionRepository.findById(communityQuestionId).orElseThrow(() -> new PeepApiException(ErrorCode.INVALID_PARAMETER));

        StudentCommunityQuestion studentCommunityQuestion = StudentCommunityQuestion.of(writer, chosen, communityQuestion);

        studentCommunityQuestionRepository.save(studentCommunityQuestion);

        return "Success choose student";

    }

    public String randomChooseStudent(String myId, String userId, Long studentQuestionId) {

        Student chosen = studentRepository.findByUserId(userId).orElseThrow(() -> new PeepApiException(ErrorCode.USER_NOT_FOUND));

        StudentQuestion studentQuestion = studentQuestionRepository.findById(studentQuestionId).orElseThrow(() -> new PeepApiException(ErrorCode.RESOURCE_NOT_FOUND, "Cannot find Question"));

        if(!Objects.equals(studentQuestion.getWriter().getUserId(), myId)) {
            throw new PeepApiException(ErrorCode.INVALID_PARAMETER);
        }
        studentQuestion.setChosen(chosen);

        studentQuestionRepository.save(studentQuestion);

        return "Success choose student";
    }


    public HomeQuestionListResponse getQuestionList(String myId) {


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

        return HomeQuestionListResponse.of(communityQuestionDtoList, studentQuestionList);
    }

    public List<ChosenQuestionResponse> getChosenQuestionList(String myId) {

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

        return questionResponseList;
    }

    public String getCommonQuestionWriterHint(String myId, Long scqId) {

        StudentCommunityQuestion scq = studentCommunityQuestionRepository.findById(scqId).orElseThrow(() -> new PeepApiException(ErrorCode.INVALID_PARAMETER));

        if(!Objects.equals(scq.getChosenInCommunity().getUserId(), myId)) {
            throw new PeepApiException(ErrorCode.INVALID_PARAMETER);
        }

        return hintUtils.getHint(scq.getWriterInCommunity(), 7);
    }

    public String getRandomQuestionWriterHint(String myId, Long sqId) {
        StudentQuestion sq = studentQuestionRepository.findById(sqId).orElseThrow(() -> new PeepApiException(ErrorCode.INVALID_PARAMETER));

        if(!Objects.equals(sq.getChosen().getUserId(), myId)) {
            throw new PeepApiException(ErrorCode.INVALID_PARAMETER);
        }

        return hintUtils.getHint(sq.getWriter(), 8);

    }



    //TODO: 대용량 처리가 예상됨으로 spring batch 도입 고려
    //TODO: 모든 사용자에게 매시간마다 업데이트 하기에 너무 많은 용량을 차지할것이 예상되어 답변한 대상으로만 질문 업데이트 고려
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
