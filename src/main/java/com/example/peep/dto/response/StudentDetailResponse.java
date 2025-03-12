package com.example.peep.dto.response;

import com.example.peep.domain.Student;
import com.example.peep.dto.HashtagDto;
import com.example.peep.dto.PhotoDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record StudentDetailResponse(
        String userId,
        String name,
        PhotoDto photoDto,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean isFollowedByMe,
        int followerCount,
        int followingCount,
        List<HashtagDto> hashtagDtoList,
        int grade,
        int myClass

) {

    public static StudentDetailResponse from(Student student, Boolean isFollowedByMe, int followerCount, int followingCount, List<HashtagDto> hashtagDtoList) {

        return new StudentDetailResponse(
                student.getUserId(),
                student.getName(),
                PhotoDto.from(student.getPhoto()),
                isFollowedByMe,
                followerCount,
                followingCount,
                hashtagDtoList,
                student.getGrade(),
                student.getMyClass()
                );
    }

}
