package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.*;
import com.example.peep.dto.CoinDto;
import com.example.peep.dto.PhotoDto;
import com.example.peep.dto.SchoolDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private SchoolRepository schoolRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private FollowRepository followRepository;
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

    @DisplayName("delete - 계정 탈퇴")
    @Test
    void givenAccessToken_whenDeleteStudent_thenDelete() {

        //Given
        String token = "token";
        Coin coin = Coin.of(0);
        Photo photo = Photo.of("dummy");
        Student std1 = Student.of("jihoon", "1234", "지훈", null, coin , photo, 3, 1, "01012345567");
        Student std2 = Student.of("minji", "1234", "민지", null, coin , photo, 3, 2, "01012345567");
        Student std3 = Student.of("inseo", "1234", "인서", null, coin , photo, 3, 3, "01012345567");

        Follow follow1 = Follow.of(std1,std2);
        Follow follow2 = Follow.of(std1,std3);
        Follow follow3 = Follow.of(std2,std1);
        Follow follow4 = Follow.of(std3,std1);

        List<Follow> followings = new ArrayList<>();
        List<Follow> followers = new ArrayList<>();

        followings.add(follow1);
        followings.add(follow2);
        followers.add(follow3);
        followers.add(follow4);

        given(jwtTokenProvider.getUserId(token)).willReturn("jihoon");
        given(studentRepository.findByUserId("jihoon")).willReturn(Optional.of(std1));
        given(followRepository.findAllByFollowingUserId("jihoon")).willReturn(followings);
        given(followRepository.findAllByFollowerUserId("jihoon")).willReturn(followers);
        //When
        studentService.deleteStudent(token);

        //Then
        ArgumentCaptor<Student> studentArgument = ArgumentCaptor.forClass(Student.class);
        ArgumentCaptor<Follow> followArgument = ArgumentCaptor.forClass(Follow.class);
        then(studentRepository).should().save(studentArgument.capture());
        then(followRepository).should(times(4)).save(followArgument.capture());

    }

}