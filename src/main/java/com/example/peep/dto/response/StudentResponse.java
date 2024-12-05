package com.example.peep.dto.response;

import com.example.peep.domain.Student;
import com.example.peep.dto.PhotoDto;
import com.example.peep.dto.SchoolDto;

public record StudentResponse(
    String userId,
    String name,
    SchoolDto schoolDto,
    PhotoDto photoDto,
    int grade,
    int myClass
) {
        public static StudentResponse of(String userId, String name, SchoolDto schoolDto, int grade, int myClass) {
            return new StudentResponse(userId, name, schoolDto, null, grade, myClass);
        }

        public static StudentResponse of(String userId, String name, SchoolDto schoolDto, PhotoDto photoDto, int grade, int myClass) {
            return new StudentResponse(userId, name, schoolDto, photoDto, grade, myClass);
        }

        public static StudentResponse from(Student student) {
            return new StudentResponse(
                    student.getUserId(),
                    student.getName(),
                    SchoolDto.from(student.getSchool()),
                    PhotoDto.from(student.getPhoto()),
                    student.getGrade(),
                    student.getMyClass()
            );
        }
    }
