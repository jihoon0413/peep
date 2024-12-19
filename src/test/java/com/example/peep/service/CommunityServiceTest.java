package com.example.peep.service;

import com.example.peep.domain.Community;
import com.example.peep.domain.Question;
import com.example.peep.domain.School;
import com.example.peep.domain.mapping.CommunityQuestion;
import com.example.peep.repository.CommunityQuestionRepository;
import com.example.peep.repository.CommunityRepository;
import com.example.peep.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {

    @Mock private CommunityRepository communityRepository;
    @Mock private QuestionRepository questionRepository;
    @Mock private CommunityQuestionRepository communityQuestionRepository;

    @InjectMocks
    private CommunityService communityService;

    @DisplayName("updateRandomQuestion - 커뮤니티에 시간이 지나면 자동으로 질문 추가")
    @Test
    void given_whenUpdateRandomQuestion_thenUpdateQuestion() {
        //Given
        School school = School.of("교육청", "학교");
        Community community1 = Community.of(school, 3,1);
        Community community2 = Community.of(school, 3,1);
        Community community3 = Community.of(school, 3,1);
        List<Community> communities = new ArrayList<>();
        communities.add(community1);
        communities.add(community2);
        communities.add(community3);
        Question q1 = Question.of("question1");
        Question q2 = Question.of("question1");
        Question q3 = Question.of("question1");
        List<Question> qList = new ArrayList<>();
        qList.add(q1);
        qList.add(q2);
        qList.add(q3);
        given(communityRepository.findAll()).willReturn(communities);
        given(questionRepository.findRandomQuestions()).willReturn(qList);
        //When
        communityService.updateRandomQuestion();
        //Then
        then(communityQuestionRepository).should().saveAll(anyList());

    }

}