package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.*;
import com.example.peep.domain.mapping.StudentCommunity;
import com.example.peep.domain.mapping.StudentHashtag;
import com.example.peep.dto.*;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.domain.enumType.HashtagType;
import com.example.peep.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private SchoolRepository schoolRepository;
    @Mock private CoinRepository coinRepository;
    @Mock private CommunityRepository communityRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private FollowRepository followRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private PhotoRepository photoRepository;
    @Mock private HashtagRepository hashtagRepository;
    @Mock private StudentHashtagRepository studentHashtagRepository;
    @Mock private StudentCommunityRepository studentCommunityRepository;

    @InjectMocks
    private StudentService studentService;

    @DisplayName("newStudent - 회원가입 후 저장 확인")
    @Test
    void givenStudent_whenNewStudent_thenGetStudent() {

//        //Given
//        SchoolDto schoolDto = SchoolDto.of(
//                1L,
//                "광주광역시교육청",
//                "광주고등학교"
//        );
//        HashtagDto hash1 = HashtagDto.of(1L, "soccer", HashtagType.HOBBY);
//        HashtagDto hash2 = HashtagDto.of(2L, "baseball", HashtagType.HOBBY);
//        HashtagDto hash3 = HashtagDto.of(3L, "tennis", HashtagType.HOBBY);
//        Hashtag hash = Hashtag.of("tennis", HashtagType.HOBBY);
//        Set<HashtagDto> hashtagDtos = Set.of(hash1, hash2, hash3);
//        PhotoDto photoDto = PhotoDto.of("dummy");
//        CoinDto coinDto = CoinDto.of(0);
//        StudentDto studentDto = StudentDto.of(
//                "wlgns",
//                "1234",
//                "지훈",
//                schoolDto,
//                coinDto,
//                photoDto,
//                hashtagDtos,
//                3,
//                1,
//                "010-1234-1234"
//        );
//        given(schoolRepository.getReferenceById(schoolDto.id())).willReturn(School.of("광주광역시교육청","광주고등학교"));
//        given(passwordEncoder.encode("1234")).willReturn("$2a$10$9mAxfCvr.rzFJMTm/xBBse2/o42tV9dCwxQw4XAjNOAQXL9tIDjKC");
////        given(studentHashtagRepository.findByStudentUserIdAndHashtagId(studentDto.userId(), anyLong())).willReturn(null);
//        given(hashtagRepository.findById(anyLong())).willReturn(Optional.of(hash));
//        //When
//        studentService.newStudent(studentDto);
//
//        //Then
//        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
//        ArgumentCaptor<Coin> coinCaptor = ArgumentCaptor.forClass(Coin.class);
//        ArgumentCaptor<StudentHashtag> studentHashtagCaptor = ArgumentCaptor.forClass(StudentHashtag.class);
//        ArgumentCaptor<Community> communityCaptor = ArgumentCaptor.forClass(Community.class);
//        ArgumentCaptor<StudentCommunity> studentCommunityCaptor = ArgumentCaptor.forClass(StudentCommunity.class);
//
//        then(schoolRepository).should().getReferenceById(schoolDto.id());
//        then(studentRepository).should().save(studentCaptor.capture());
//        then(coinRepository).should().save(coinCaptor.capture());
//        then(studentHashtagRepository).should(times(3)).save(studentHashtagCaptor.capture());
//        then(communityRepository).should().save(communityCaptor.capture());
//        then(studentCommunityRepository).should().save(studentCommunityCaptor.capture());

    }

    @DisplayName("modifyStudent - 학생 정보 업데이트")
    @Test
    void givenUpdatedStudentDto_whenModifyStudent_thenUpdateStudent() {

        //Given
//        School oldSchool = School.of("광주광역시교육청", "광주고등학교");
//        SchoolDto newSchoolDto = SchoolDto.of(2L, "광주광역시교육청", "조선대학교부속중학교");
//        Photo oldPhoto = Photo.of(null);
//        PhotoDto newPhoto = PhotoDto.of("photo");
//        Student oldStudent = Student.of("jihoon", "1234", "신지훈", oldSchool, Coin.of(0), oldPhoto, 3, 1, "01087654321");
//        StudentDto updatedDto = StudentDto.of("jihoon", "1234", "신지훈", newSchoolDto, CoinDto.of(0), newPhoto, null, 3, 3, "01087654321");
//
//        given(studentRepository.findByUserId("jihoon")).willReturn(Optional.of(oldStudent));
//        given(schoolRepository.findById(newSchoolDto.id())).willReturn(Optional.of(newSchoolDto.toEntity()));
//        given(photoRepository.findById(oldPhoto.getId())).willReturn(Optional.of(oldPhoto));
//        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
//        //When
//        studentService.modifyStudent("token", updatedDto);
//
//        //Then
//        then(studentRepository).should(times(1)).save(oldStudent);
//
//        assertThat(oldStudent.getSchool().getSchoolName()).isEqualTo("조선대학교부속중학교");
//        assertThat(oldStudent.getPhoto().getPhotoUrl()).isEqualTo("photo");
//        assertThat(oldStudent.getGrade()).isEqualTo(3);
//        assertThat(oldStudent.getMyClass()).isEqualTo(3);
    }

    @DisplayName("deleteStudent - 계정 탈퇴")
    @Test
    void givenAccessToken_whenDeleteStudent_thenDelete() {

//        //Given
//        String token = "token";
//        Coin coin = Coin.of(0);
//        Photo photo = Photo.of("dummy");
//        Student std1 = Student.of("jihoon", "1234", "지훈", null, coin , photo, 3, 1, "01012345567");
//        Student std2 = Student.of("minji", "1234", "민지", null, coin , photo, 3, 2, "01012345567");
//        Student std3 = Student.of("inseo", "1234", "인서", null, coin , photo, 3, 3, "01012345567");
//
//        Follow follow1 = Follow.of(std1,std2);
//        Follow follow2 = Follow.of(std1,std3);
//        Follow follow3 = Follow.of(std2,std1);
//        Follow follow4 = Follow.of(std3,std1);
//
//        List<Follow> followings = new ArrayList<>();
//        List<Follow> followers = new ArrayList<>();
//
//        followings.add(follow1);
//        followings.add(follow2);
//        followers.add(follow3);
//        followers.add(follow4);
//
//        given(jwtTokenProvider.getUserId(token)).willReturn("jihoon");
//        given(studentRepository.findByUserId("jihoon")).willReturn(Optional.of(std1));
//        given(followRepository.findAllByFollowingUserId("jihoon")).willReturn(followings);
//        given(followRepository.findAllByFollowerUserId("jihoon")).willReturn(followers);
//        //When
//        studentService.deleteStudent(token);
//
//        //Then
//        ArgumentCaptor<Student> studentArgument = ArgumentCaptor.forClass(Student.class);
//        ArgumentCaptor<Follow> followArgument = ArgumentCaptor.forClass(Follow.class);
//        then(studentRepository).should().save(studentArgument.capture());
//        then(followRepository).should(times(4)).save(followArgument.capture());

    }

    @DisplayName("getFourStudents - 질문에서 선택할 랜덤의 4명 학생들")
    @Test
    void givenTokenAndCommunityId_whenGetFourStudents_thenReturnStudentResponseList() {
        //Given
//        School school = School.of("교육청", "학교");
//        Photo photo = Photo.of("dummy");
//        Coin coin = Coin.of(0);
//        Student s1 = Student.of("minji", "1234", "민지", school,coin,photo,3,1,"01012345678");
//        Student s2 = Student.of("inseo", "1234", "인서", school,coin,photo,3,1,"01012345678");
//        Student s3 = Student.of("soomin", "1234", "수민", school,coin,photo,3,1,"01012345678");
//        List<Student> list = new ArrayList<>();
//        list.add(s1);
//        list.add(s2);
//        list.add(s3);
//        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
//        given(studentRepository.findRandomFourStudents(1L, "jihoon")).willReturn(list);
//        //When
//        List<StudentResponse> result = studentService.getFourStudents("token", 1L).getBody();
//        //Then
//        assertThat(result.size()).isEqualTo(3);
    }
}