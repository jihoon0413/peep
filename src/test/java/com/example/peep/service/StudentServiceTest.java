package com.example.peep.service;

import com.example.peep.domain.School;
import com.example.peep.domain.Student;
import com.example.peep.dto.SchoolDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.SchoolRepository;
import com.example.peep.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void given_when_then() {

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
        given(studentRepository.findByUserId("wlgns")).willReturn(Optional.of(Student.of("wlgns", "1234", "지훈", null, 3, 1, "1234-1234")));
        studentService.newStudent(studentDto);
        //Then
        Student student = studentRepository.findByUserId("wlgns").get();
        assertThat(student.getUserId()).isEqualTo("wlgns");
        then(studentRepository).should().findByUserId("wlgns");

//        //Given
//        StudentDto studentDto = StudentDto.of(
//                "wlgns",
//                "1234",
//                "지훈",
//                null,
//                3,
//                1,
//                "010-1234-1234"
//        );
//
//        studentService.newStudent(studentDto);
//
//        assertThat(studentRepository.findByUserId("wlgns").get().getUserId()).isEqualTo("wlgns");

    }

}