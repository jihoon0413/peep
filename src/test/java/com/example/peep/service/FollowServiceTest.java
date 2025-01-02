package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.*;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.repository.BlockRepository;
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
    @Mock BlockRepository blockRepository;

    @InjectMocks FollowService followService;

    @DisplayName("newFollow - 새로운 팔로우 추가")
    @Test
    void given_whenNewFollow_thenSaveNewFollow() {

        //Given
        Student follower = makeStudent("minji", null, null);
        Student following = makeStudent("jihoon", null, null);
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
        Student std1 = makeStudent("minji", null, null);
        Student std2 = makeStudent("jihoon", null, null);

        Follow follow = Follow.of(std1, std2);
        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
        given(followRepository.findByFollowerUserIdAndFollowingUserId("jihoon", userId)).willReturn(Optional.of(follow));

        //When
        followService.unFollow("token", userId);

        //Then
        ArgumentCaptor<Follow> followCaptor = ArgumentCaptor.forClass(Follow.class);
        then(followRepository).should().save(followCaptor.capture());
    }

    @DisplayName("getFollowingList - 팔로잉 목록 조회")
    @Test
    void given_whenGetFollowingList_thenReturnFollowingList() {
        //Given
        String userId = "jihoon";
        List<Follow> followingList = new ArrayList<>();
        School school = makeSchool();
        Photo photo = makePhoto();

        Student std = makeStudent("jihoon",school,photo);
        Student std1 = makeStudent("minji", school, photo);
        Student std2 = makeStudent("inseo", school, photo);
        followingList.add(Follow.of(std, std1));
        followingList.add(Follow.of(std, std2));

        given(followRepository.findAllByFollowerUserId("jihoon")).willReturn(followingList);
        given(studentRepository.findById(std1.getId())).willReturn(Optional.of(std1));
        given(studentRepository.findById(std2.getId())).willReturn(Optional.of(std2));
        //When
        List<StudentResponse> studentDtoList = followService.getFollowingList(userId).getBody();
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
        School school = makeSchool();
        Photo photo = makePhoto();

        Student std = makeStudent("jihoon",school,photo);
        Student std1 = makeStudent("minji", school, photo);
        Student std2 = makeStudent("inseo", school, photo);

        followerList.add(Follow.of(std1, std));
        followerList.add(Follow.of(std2, std));

        given(followRepository.findAllByFollowingUserId("jihoon")).willReturn(followerList);
        given(studentRepository.findById(std1.getId())).willReturn(Optional.of(std1));
        given(studentRepository.findById(std2.getId())).willReturn(Optional.of(std2));
        //When
        List<StudentResponse> studentDtoList = followService.getFollowerList(userId).getBody();
        //Then
        assertThat(studentDtoList.size()).isEqualTo(2);
//        assertThat(studentDtoList.get(0).userId()).isEqualTo("minji");
        assertThat(studentDtoList.get(1).userId()).isEqualTo("inseo");

    }

    @DisplayName("block - 차단 기능")
    @Test
    void givenTokenAndId_whenBlock_thenSaveNewBlock() {

        //Given
        String token = "token";
        String userId = "sample";

        School school = makeSchool();
        Photo photo = makePhoto();
        Student std1 = makeStudent("jihoon",school, photo);
        Student std2 = makeStudent("sample", school, photo);

        given(jwtTokenProvider.getUserId(token)).willReturn("jihoon");
        given(studentRepository.findByUserId("jihoon")).willReturn(Optional.ofNullable(std1));
        given(studentRepository.findByUserId("sample")).willReturn(Optional.ofNullable(std2));
        //When
        followService.blockFollow(token, userId);
        //Then
        ArgumentCaptor<Block> argumentCaptor = ArgumentCaptor.forClass(Block.class);
        then(blockRepository).should().save(argumentCaptor.capture());

    }

    @DisplayName("unBlock - 차단 해제 기능")
    @Test
    void givenTokenAndId_whenUnBlock_thenDeleteBlock() {

        //Given
        String token = "token";
        String userId = "sample";
        School school = makeSchool();
        Photo photo = makePhoto();
        Student std1 = makeStudent("jihoon", school, photo);
        Student std2 = makeStudent("sample", school, photo);
        Block block = Block.of(std1,std2);
        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
        given(blockRepository.deleteByBlockerUserIdAndBlockedUserId("jihoon", "sample")).willReturn(Optional.of(block));
        //When
        followService.unBlockFollow(token, userId);
        //Then
        then(blockRepository).should().deleteByBlockerUserIdAndBlockedUserId("jihoon", "sample");
    }

    Student makeStudent(String userId, School school, Photo photo) {

        return Student.of(userId, "1234", "지훈", school, null, photo, 3,1,"01012345678");
    }

    School makeSchool() {
        return School.of("광주광역시교육청", "광주고등학교");
    }

    Photo makePhoto() {
        return Photo.of("dummy");
    }
}