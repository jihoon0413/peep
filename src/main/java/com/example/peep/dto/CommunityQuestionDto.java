package com.example.peep.dto;

import com.example.peep.domain.mapping.CommunityQuestion;

public record CommunityQuestionDto(
        Long id,
        CommunityDto communityDto,
        QuestionDto questionDto
) {

    public static CommunityQuestionDto of(Long id, CommunityDto communityDto, QuestionDto questionDto) {
        return new CommunityQuestionDto(id, communityDto, questionDto);
    }

    public static CommunityQuestionDto from(CommunityQuestion communityQuestion) {
        return CommunityQuestionDto.of(
                communityQuestion.getId(),
                CommunityDto.from(communityQuestion.getCommunity()),
                QuestionDto.from(communityQuestion.getQuestion()));
    }

}
