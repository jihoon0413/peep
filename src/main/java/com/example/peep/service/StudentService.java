package com.example.peep.service;

import com.example.peep.domain.School;
import com.example.peep.domain.Student;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.SchoolRepository;
import com.example.peep.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    public void newStudent(StudentDto studentDto) {

        Long schoolId = studentDto.schoolDto().id();
        School school = schoolRepository.getReferenceById(schoolId);
        Student student = studentDto.toEntity(school);

        //TODO: 코인, 사진 데이터 만들기
        //TODO: 비밀번호 암호화
        studentRepository.save(student);
    }

    public String getStudent(String userId) {
        System.out.println(userId);
        Student student = studentRepository.findById(1L).orElse(null);
        System.out.println(student.getUserId());
        return student.getUserId();
    }
}
