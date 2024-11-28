package com.example.peep.service;

import com.example.peep.domain.School;
import com.example.peep.domain.Student;
import com.example.peep.dto.SchoolDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.SchoolRepository;
import com.example.peep.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private SchoolRepository schoolRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    @DisplayName("회원가입 후 저장 확인")
    @Test
    void givenStudent_whenNewStudent_thenGetStudent() {

        //Given
        SchoolDto schoolDto = SchoolDto.of(
                1L,
                "광주광역시교육청",
                "광주고등학교"
        );
        StudentDto studentDto = StudentDto.of(
                "wlgns",
                "1234",
                "지훈",
                schoolDto,
                3,
                1,
                "010-1234-1234"
        );
        //When
        given(schoolRepository.getReferenceById(1L)).willReturn(School.of("광주광역시교육청","광주고등학교"));
        given(studentRepository.findByUserId("wlgns")).willReturn(Optional.of(Student.of("wlgns", "1234", "지훈", null, null, null, 3, 1, "1234-1234")));
        given(passwordEncoder.encode("1234")).willReturn("$2a$10$9mAxfCvr.rzFJMTm/xBBse2/o42tV9dCwxQw4XAjNOAQXL9tIDjKC");
        studentService.newStudent(studentDto);

        //Then
        Student student = studentRepository.findByUserId("wlgns").get();
        assertThat(student.getUserId()).isEqualTo("wlgns");
        then(studentRepository).should().findByUserId("wlgns");
    }

}