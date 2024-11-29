package com.example.peep.service;

import com.example.peep.domain.Coin;
import com.example.peep.domain.Photo;
import com.example.peep.domain.School;
import com.example.peep.domain.Student;
import com.example.peep.dto.CoinDto;
import com.example.peep.dto.PhotoDto;
import com.example.peep.dto.SchoolDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.CoinRepository;
import com.example.peep.repository.PhotoRepository;
import com.example.peep.repository.SchoolRepository;
import com.example.peep.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    @Mock private CoinRepository coinRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private PhotoRepository photoRepository;

    @InjectMocks
    private StudentService studentService;

    @DisplayName("newStudent - 회원가입 후 저장 확인")
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
        given(schoolRepository.getReferenceById(schoolDto.id())).willReturn(School.of("광주광역시교육청","광주고등학교"));
        given(passwordEncoder.encode("1234")).willReturn("$2a$10$9mAxfCvr.rzFJMTm/xBBse2/o42tV9dCwxQw4XAjNOAQXL9tIDjKC");

        //When
        studentService.newStudent(studentDto);

        //Then
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

        then(schoolRepository).should().getReferenceById(schoolDto.id());
        then(studentRepository).should().save(studentCaptor.capture());
    }

    @DisplayName("modify - 학생 정보 업데이트")
    @Test
    void givenUpdatedStudentDto_whenModifyStudent_thenUpdateStudent() {

        //Given
        School oldSchool = School.of("광주광역시교육청", "광주고등학교");
        SchoolDto newSchoolDto = SchoolDto.of(2L, "광주광역시교육청", "조선대학교부속중학교");
        Photo oldPhoto = Photo.of(null);
        PhotoDto newPhoto = PhotoDto.of("photo");
        Student oldStudent = Student.of("jihoon", "1234", "신지훈", oldSchool, Coin.of(0), oldPhoto, 3, 1, "01087654321");
        StudentDto updatedDto = StudentDto.of("jihoon", "1234", "신지훈", newSchoolDto, CoinDto.of(0), newPhoto, 3, 3, "01087654321");

        given(studentRepository.findByUserId("jihoon")).willReturn(Optional.of(oldStudent));
        given(schoolRepository.findById(newSchoolDto.id())).willReturn(Optional.of(newSchoolDto.toEntity()));
        given(photoRepository.findById(oldPhoto.getId())).willReturn(Optional.of(oldPhoto));
        //When
        studentService.modifyStudent(updatedDto);

        //Then
        then(studentRepository).should(times(1)).save(oldStudent);

        assertThat(oldStudent.getSchool().getSchoolName()).isEqualTo("조선대학교부속중학교");
        assertThat(oldStudent.getPhoto().getPhotoUrl()).isEqualTo("photo");
        assertThat(oldStudent.getGrade()).isEqualTo(3);
        assertThat(oldStudent.getMyClass()).isEqualTo(3);
    }

}