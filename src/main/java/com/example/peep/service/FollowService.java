package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.Block;
import com.example.peep.domain.Follow;
import com.example.peep.domain.Student;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.errors.errorcode.CommonErrorCode;
import com.example.peep.errors.exception.RestApiException;
import com.example.peep.repository.BlockRepository;
import com.example.peep.repository.FollowRepository;
import com.example.peep.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final StudentRepository studentRepository;
    private final FollowRepository followRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BlockRepository blockRepository;

    public ResponseEntity<Void> newFollow(String token, String userId) {

        String myId = jwtTokenProvider.getUserId(token);


        Follow followRecord = followRepository.findByFollowerUserIdAndFollowingUserId(myId, userId).orElse(null);
        Block block = blockRepository.findByBlockerUserIdAndBlockedUserId(userId, myId).orElse(null);

        if(followRecord == null && block == null) {
            Student follower = studentRepository.findByUserId(myId).orElseThrow(() -> new IllegalArgumentException("올바르지 않은 접근입니다."));
            Student following = studentRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("올바르지 않은 접근입니다."));
            if(!following.getIsDeleted()) {
                Follow follow = Follow.of(follower, following);
                followRepository.save(follow);
            } else {
                throw new IllegalArgumentException("존재하지 않은 계정입니다.");
            }
        }
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> unFollow(String token, String userId) {

        String myId = jwtTokenProvider.getUserId(token);

        Follow follow = followRepository.findByFollowerUserIdAndFollowingUserId(myId, userId).orElseThrow();
        follow.setIsDeleted(true);
        followRepository.save(follow);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<StudentResponse>> getFollowingList(String userId) {
        return ResponseEntity.ok(followRepository.findAllByFollowerUserId(userId)
                .stream()
                .map(follow -> studentRepository.findById(follow.getFollowing().getId())
                        .filter(student -> !student.getIsDeleted())
                        .map(StudentResponse::from)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList());
    }

    public ResponseEntity<List<StudentResponse>> getFollowerList(String userId) {
        return ResponseEntity.ok(followRepository.findAllByFollowingUserId(userId)
                .stream()
                .map(follow -> studentRepository.findById(follow.getFollower().getId())
                        .filter(student -> !student.getIsDeleted())
                        .map(student -> StudentResponse.from(student, isFollowedByMe(userId, student.getUserId())))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList());
    }

    public ResponseEntity<?> blockFollow(String token, String userId) {

        String myId = jwtTokenProvider.getUserId(token);

        Student blocker = studentRepository.findByUserId(myId).orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
        Student blocked = studentRepository.findByUserId(userId).orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

        Block block = Block.of(blocker, blocked);

        blockRepository.save(block);

        return ResponseEntity.ok("Block");
    }

    public ResponseEntity<?> unBlockFollow(String token, String userId) {

        String myId = jwtTokenProvider.getUserId(token);

        blockRepository.deleteByBlockerUserIdAndBlockedUserId(myId, userId).orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

        return ResponseEntity.ok("unBlock");

    }

    private boolean isFollowedByMe(String myId, String followerId){
        Optional<Follow> follow = followRepository.findByFollowerUserIdAndFollowingUserId(myId, followerId);
        return follow.isPresent();
    }
}
