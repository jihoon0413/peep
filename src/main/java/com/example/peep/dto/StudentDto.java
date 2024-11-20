package com.example.peep.dto;

import com.example.peep.domain.Coin;
import com.example.peep.domain.Photo;
import com.example.peep.domain.School;
import com.example.peep.domain.Student;

public record StudentDto(
        String userId,
        String userPassword,
        String name,
        SchoolDto schoolDto,
        Coin coin,
        Photo photo,
        int grade,
        int myClass,
        String tel
) {
    public static StudentDto of(String userId, String userPassword, String name, SchoolDto schoolDto, int grade, int myClass, String tel) {
        return new StudentDto(userId, userPassword, name, schoolDto, null, null, grade, myClass, tel);
    }

    public static StudentDto from(Student student) {
        return new StudentDto(
                student.getUserId(),
                student.getUserPassword(),
                student.getName(),
                SchoolDto.from(student.getSchool()),
                null,
                null,
                student.getGrade(),
                student.getMyClass(),
                student.getTel()
        );
    }

    public Student toEntity(School school, Coin coin, Photo photo) {
        return Student.of(
                userId,
                userPassword,
                name,
                school,
                coin,
                photo,
                grade,
                myClass,
                tel
        );
    }
}
