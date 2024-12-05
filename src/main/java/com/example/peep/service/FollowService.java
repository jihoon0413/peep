package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.Follow;
import com.example.peep.domain.Student;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.repository.FollowRepository;
import com.example.peep.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final StudentRepository studentRepository;
    private final FollowRepository followRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void newFollow(String token, String userId) {

        String myId = jwtTokenProvider.getUserId(token);


        Follow followRecord = followRepository.findByFollowerUserIdAndFollowingUserId(myId, userId).orElse(null);

        if(followRecord == null) {
            Student follower = studentRepository.findByUserId(myId).orElseThrow(() -> new IllegalArgumentException("올바르지 않은 접근입니다."));
            Student following = studentRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("올바르지 않은 접근입니다."));
            if(!following.getIsDeleted()) {
                Follow follow = Follow.of(follower, following);
                followRepository.save(follow);
            } else {
                throw new IllegalArgumentException("존재하지 않은 계정입니다.");
            }
        }
    }

    public void unFollow(String token, String userId) {

        String myId = jwtTokenProvider.getUserId(token);

        followRepository.deleteByFollowerUserIdAndFollowingUserId(myId, userId);
    }

    public List<StudentResponse> getFollowingList(String userId) {
        return followRepository.findAllByFollowerUserId(userId)
                .stream()
                .map(follow -> studentRepository.findById(follow.getFollowing().getId())
                        .filter(student -> !student.getIsDeleted())
                        .map(StudentResponse::from)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<StudentResponse> getFollowerList(String userId) {
        return followRepository.findAllByFollowingUserId(userId)
                .stream()
                .map(follow -> studentRepository.findById(follow.getFollower().getId())
                        .filter(student -> !student.getIsDeleted())
                        .map(StudentResponse::from)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }
}
