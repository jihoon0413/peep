package com.example.peep.dto.response;

import com.example.peep.domain.Student;
import com.example.peep.dto.PhotoDto;

public record StudentResponse(
    String userId,
    String name,
    PhotoDto photoDto,
    int grade,
    int myClass
) {
        public static StudentResponse of(String userId, String name, int grade, int myClass) {
            return new StudentResponse(userId, name, null, grade, myClass);
        }

        public static StudentResponse of(String userId, String name, PhotoDto photoDto, int grade, int myClass) {
            return new StudentResponse(userId, name, photoDto, grade, myClass);
        }

        public static StudentResponse from(Student student) {
            return new StudentResponse(
                    student.getUserId(),
                    student.getName(),
                    PhotoDto.from(student.getPhoto()),
                    student.getGrade(),
                    student.getMyClass()
            );
        }
    }
