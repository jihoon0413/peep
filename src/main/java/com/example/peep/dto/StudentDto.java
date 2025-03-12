package com.example.peep.dto;

import com.example.peep.domain.Coin;
import com.example.peep.domain.Photo;
import com.example.peep.domain.School;
import com.example.peep.domain.Student;
import com.example.peep.domain.enumType.Gender;

import java.util.Set;

public record StudentDto(
        String userId,
        String userPassword,
        String name,
        Gender gender,
        SchoolDto schoolDto,
        CoinDto coinDto,
        PhotoDto photoDto,
        Set<HashtagDto> hashtagDtos,
        int grade,
        int myClass,
        String tel
) {
    public static StudentDto of(String userId, String userPassword, String name, Gender gender, SchoolDto schoolDto, int grade, int myClass, String tel) {
        return new StudentDto(userId, userPassword, name, gender, schoolDto, null, null, null, grade, myClass, tel);
    }

    public static StudentDto of(String userId, String userPassword, String name, Gender gender, SchoolDto schoolDto, CoinDto coindto, PhotoDto photoDto, Set<HashtagDto> hashtagDtos, int grade, int myClass, String tel) {
        return new StudentDto(userId, userPassword, name, gender, schoolDto, coindto, photoDto, hashtagDtos, grade, myClass, tel);
    }

    public static StudentDto from(Student student) {
        return new StudentDto(
                student.getUserId(),
                null,
                student.getName(),
                student.getGender(),
                SchoolDto.from(student.getSchool()),
                CoinDto.from(student.getCoin()),
                PhotoDto.from(student.getPhoto()),
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
                gender,
                school,
                coin,
                photo,
                grade,
                myClass,
                tel
        );
    }
}
