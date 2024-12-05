package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.Follow;
import com.example.peep.domain.Photo;
import com.example.peep.domain.School;
import com.example.peep.domain.Student;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.repository.FollowRepository;
import com.example.peep.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock StudentRepository studentRepository;
    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock FollowRepository followRepository;

    @InjectMocks FollowService followService;

    @DisplayName("newFollow - 새로운 팔로우 추가")
    @Test
    void given_whenNewFollow_thenSaveNewFollow() {

        //Given
        Student follower = Student.of("minji", "1234", "민지", null, null, null, 3,1,"01012345678");
        Student following = Student.of("jihoon", "1234", "지훈", null, null, null, 3,2,"01087654321");
        String token = "token";

        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
        given(studentRepository.findByUserId("minji")).willReturn(Optional.of(follower));
        given(studentRepository.findByUserId("jihoon")).willReturn(Optional.of(following));

        //When
        followService.newFollow(token, "minji");

        //Then
        ArgumentCaptor<Follow> followCaptor = ArgumentCaptor.forClass(Follow.class);
        then(followRepository).should().save(followCaptor.capture());

    }

    @DisplayName("unFollow - 언팔로우 기능")
    @Test
    void givenTokenAndUserId_whenUnFollow_thenDeleteFollow() {

        //Given
        String userId = "minji";
        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");

        //When
        followService.unFollow("token", userId);

        //Then
        then(followRepository).should().deleteByFollowerUserIdAndFollowingUserId("jihoon", userId);
    }

    @DisplayName("getFollowingList - 팔로잉 목록 조회")
    @Test
    void given_whenGetFollowingList_thenReturnFollowingList() {
        //Given
        String userId = "jihoon";
        List<Follow> followingList = new ArrayList<>();
        School school = School.of("광주광역시교육청", "광주고등학교");
        Photo photo = Photo.of("dummy");
        Student std = Student.of("jihoon", null, "지훈", school, null, photo, 3,1,"01012345678");
        Student std1 = Student.of("minji", null, "민지", school, null, photo, 2,1,"01012345678");
        Student std2 = Student.of("inseo", null, "인서", school, null, photo, 1, 1, "01087654321");
        followingList.add(Follow.of(std, std1));
        followingList.add(Follow.of(std, std2));

        given(followRepository.findAllByFollowerUserId("jihoon")).willReturn(followingList);
        given(studentRepository.findById(std1.getId())).willReturn(Optional.of(std1));
        given(studentRepository.findById(std2.getId())).willReturn(Optional.of(std2));
        //When
        List<StudentResponse> studentDtoList = followService.getFollowingList(userId);
        //Then
        assertThat(studentDtoList.size()).isEqualTo(2);
//        assertThat(studentDtoList.get(0).userId()).isEqualTo("minji");
        assertThat(studentDtoList.get(1).userId()).isEqualTo("inseo");

    }

    @DisplayName("getFollowerList - 팔로워 목록 조회")
    @Test
    void given_whenGetFollowerList_thenReturnFollowerList() {
        //Given
        String userId = "jihoon";
        List<Follow> followerList = new ArrayList<>();
        School school = School.of("광주광역시교육청", "광주고등학교");
        Photo photo = Photo.of("dummy");
        Student std = Student.of("jihoon", null, "지훈", school, null, photo, 3,1,"01012345678");
        Student std1 = Student.of("minji", null, "민지", school, null, photo, 2,1,"01012345678");
        Student std2 = Student.of("inseo", null, "인서", school, null, photo, 1, 1, "01087654321");
        followerList.add(Follow.of(std1, std));
        followerList.add(Follow.of(std2, std));

        given(followRepository.findAllByFollowingUserId("jihoon")).willReturn(followerList);
        given(studentRepository.findById(std1.getId())).willReturn(Optional.of(std1));
        given(studentRepository.findById(std2.getId())).willReturn(Optional.of(std2));
        //When
        List<StudentResponse> studentDtoList = followService.getFollowerList(userId);
        //Then
        assertThat(studentDtoList.size()).isEqualTo(2);
//        assertThat(studentDtoList.get(0).userId()).isEqualTo("minji");
        assertThat(studentDtoList.get(1).userId()).isEqualTo("inseo");

    }
}