package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.Community;
import com.example.peep.domain.Question;
import com.example.peep.domain.School;
import com.example.peep.domain.Student;
import com.example.peep.domain.mapping.CommunityQuestion;
import com.example.peep.domain.mapping.StudentCommunity;
import com.example.peep.domain.mapping.StudentCommunityQuestion;
import com.example.peep.dto.CommunityQuestionDto;
import com.example.peep.repository.CommunityQuestionRepository;
import com.example.peep.repository.StudentCommunityQuestionRepository;
import com.example.peep.repository.StudentCommunityRepository;
import com.example.peep.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private StudentRepository studentRepository;
    @Mock private StudentCommunityRepository studentCommunityRepository;
    @Mock private CommunityQuestionRepository communityQuestionRepository;
    @Mock private StudentCommunityQuestionRepository studentCommunityQuestionRepository;

    @InjectMocks
    private QuestionService questionService;

    @DisplayName("choice - 질문에서 학생선택")
    @Test
    void givenTokenAndQuestion_whenChoice_thenChooseStudentAndSave() {
        //Given
        String token = "token";
        Long id = 1L;
        Student std1 = Student.of("jihoon", "1234" ,"지훈", null, null, null, 3,1, "01012345678");
        Student std2 = Student.of("minji", "1234" ,"민지", null, null, null, 3,1, "01012345678");
        School school = School.of("교육청", "광주고");
        Community c = Community.of(school,3,1);
        Question q = Question.of("question1");
        CommunityQuestion cq = CommunityQuestion.of(c, q);

        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
        given(studentRepository.findByUserId("jihoon")).willReturn(Optional.of(std1));
        given(studentRepository.findByUserId("minji")).willReturn(Optional.of(std2));
        given(communityQuestionRepository.findById(id)).willReturn(Optional.of(cq));

        //When
        questionService.chooseStudent(token, "minji", id);
        //Then
        ArgumentCaptor<StudentCommunityQuestion> captor = ArgumentCaptor.forClass(StudentCommunityQuestion.class);
        then(studentCommunityQuestionRepository).should().save(captor.capture());

    }

    @DisplayName("getQuestionList - 질문리스트 불러오기")
    @Test
    void givenToken_whenGetQuestionList_thenReturnCommunityQuestionDtoList() {
        //Given
        School school = School.of("교육청", "광주고");
        Student std1 = Student.of("jihoon", "1234" ,"지훈", null, null, null, 3,1, "01012345678");
        Community c1 = Community.of(school, 3, 1);
        Community c2 = Community.of(school, 3, 1);
        c1.setId(1L);
        c2.setId(2L);
        Question q1 = Question.of("question1");
        Question q2 = Question.of("question2");
        Question q3 = Question.of("question3");
        Question q4 = Question.of("question4");
        Question q5 = Question.of("question5");
        StudentCommunity sc1 = StudentCommunity.of(std1, c1);
        StudentCommunity sc2 = StudentCommunity.of(std1, c2);
        CommunityQuestion cq1 = CommunityQuestion.of(c1, q1);
        CommunityQuestion cq2 = CommunityQuestion.of(c1, q2);
        CommunityQuestion cq3 = CommunityQuestion.of(c1, q3);
        CommunityQuestion cq4 = CommunityQuestion.of(c2, q4);
        CommunityQuestion cq5 = CommunityQuestion.of(c2, q5);
        List<CommunityQuestion> cqList1 = new ArrayList<>();
        List<CommunityQuestion> cqList2 = new ArrayList<>();
        List<StudentCommunity> clist = new ArrayList<>();
        clist.add(sc1);
        clist.add(sc2);
        cqList1.add(cq1);
        cqList1.add(cq2);
        cqList1.add(cq3);
        cqList2.add(cq4);
        cqList2.add(cq5);

        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
        given(studentCommunityRepository.findAllByStudentUserId("jihoon")).willReturn(clist);
        given(communityQuestionRepository.findAllByCommunityId(1L)).willReturn(cqList1);
        given(communityQuestionRepository.findAllByCommunityId(2L)).willReturn(cqList2);
        //When
        List<CommunityQuestionDto> result = questionService.getQuestionList("token").getBody();
        //Then
        assertThat(result.size()).isEqualTo(5);

    }

}