package com.example.peep.fixture;

import com.example.peep.domain.Student;
import com.example.peep.domain.enumType.Gender;
import com.example.peep.dto.StudentDto;

public class StudentFixture {

    public static StudentDto getDto(String userId, String name, Gender gender) {
        return StudentDto.of(userId, "1234", name, gender, null, 0, 0, null);
    }

    public static Student getStudent(String userId, String name, Gender gender) {
        return Student.of(userId, "1234", name ,gender, null, null, null, 0,0, null);
    }

}
