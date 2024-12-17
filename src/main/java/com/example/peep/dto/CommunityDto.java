package com.example.peep.dto;

import com.example.peep.domain.Community;

public record CommunityDto(
        SchoolDto schoolDto,
        int grade,
        int myClass
) {

    public static CommunityDto of(SchoolDto schoolDto, int grade, int myClass) {
        return new CommunityDto(schoolDto, grade, myClass);
    }

    public static CommunityDto from(Community community) {
        return CommunityDto.of(SchoolDto.from(community.getSchool()), community.getGrade(), community.getMyClass());
    }
}
