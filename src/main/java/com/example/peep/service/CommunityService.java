package com.example.peep.service;

import com.example.peep.domain.Community;
import com.example.peep.domain.Question;
import com.example.peep.domain.mapping.CommunityQuestion;
import com.example.peep.repository.CommunityQuestionRepository;
import com.example.peep.repository.CommunityRepository;
import com.example.peep.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final QuestionRepository questionRepository;
    private final CommunityQuestionRepository communityQuestionRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3, initialDelay = 1000 * 30)
    public void updateRandomQuestion() {
        List<Community> communities = communityRepository.findAll();
        List<CommunityQuestion> communityQuestions = new ArrayList<>();

        for(Community community : communities) {
            List<Question> questions = questionRepository.findRandomQuestions();
            for (Question question : questions) {
                CommunityQuestion communityQuestion = CommunityQuestion.of(community, question);
                communityQuestions.add(communityQuestion);
            }
        }
        communityQuestionRepository.saveAll(communityQuestions);
    }
    

}
