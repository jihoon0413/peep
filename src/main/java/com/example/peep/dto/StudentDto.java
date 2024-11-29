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
        CoinDto coinDto,
        PhotoDto photoDto,
        int grade,
        int myClass,
        String tel
) {
    public static StudentDto of(String userId, String userPassword, String name, SchoolDto schoolDto, int grade, int myClass, String tel) {
        return new StudentDto(userId, userPassword, name, schoolDto, null, null, grade, myClass, tel);
    }

    public static StudentDto of(String userId, String userPassword, String name, SchoolDto schoolDto, CoinDto coindto, PhotoDto photoDto, int grade, int myClass, String tel) {
        return new StudentDto(userId, userPassword, name, schoolDto, coindto, photoDto, grade, myClass, tel);
    }

    public static StudentDto from(Student student) {
        return new StudentDto(
                student.getUserId(),
                null,
                student.getName(),
                SchoolDto.from(student.getSchool()),
                CoinDto.from(student.getCoin()),
                PhotoDto.from(student.getPhoto()),
                student.getGrade(),
                student.getMyClass(),
                student.getTel()
        );
    }

    public Student toEntity() {
        return Student.of(
                userId,
                userPassword,
                name,
                schoolDto.toEntity(),
                coinDto.toEntity(),
                photoDto.toEntity(),
                grade,
                myClass,
                tel
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
