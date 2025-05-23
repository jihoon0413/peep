package com.example.peep.dto.response;

import com.example.peep.domain.Student;
import com.example.peep.dto.PhotoDto;
import com.fasterxml.jackson.annotation.JsonInclude;

public record StudentResponse(
    String userId,
    String name,
    String photoUrl,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isFollowedByMe
) {
//        public static StudentResponse of(String userId, String name, int grade, int myClass) {
//            return new StudentResponse(userId, name, null, grade, myClass);
//        }
//
//        public static StudentResponse of(String userId, String name, PhotoDto photoDto, int grade, int myClass) {
//            return new StudentResponse(userId, name, photoDto, grade, myClass);
//        }

    public static StudentResponse from(Student student, Boolean isFollowedByMe) {
        return new StudentResponse(
                student.getUserId(),
                student.getName(),
                student.getPhoto().getPhotoUrl(),
                isFollowedByMe
        );
    }

    public static StudentResponse from(Student student) {
            return from(student, null);
    }
}
