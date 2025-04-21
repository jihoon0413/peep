package com.example.peep.service;

import com.example.peep.domain.Block;
import com.example.peep.domain.Follow;
import com.example.peep.domain.Student;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.errors.ErrorCode;
import com.example.peep.errors.PeepApiException;
import com.example.peep.repository.BlockRepository;
import com.example.peep.repository.FollowRepository;
import com.example.peep.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final BlockRepository blockRepository;

    public String newFollow(String myId, String userId) {

        Follow followRecord = followRepository.findByFollowerUserIdAndFollowingUserId(myId, userId).orElse(null);
        Block block = blockRepository.findByBlockerUserIdAndBlockedUserId(userId, myId).orElse(null);

        if(block != null) {
            throw new PeepApiException(ErrorCode.FORBIDDEN);
        }
        if(followRecord != null) {
            throw new PeepApiException(ErrorCode.ALREADY_EXECUTION);
        }

        Student follower = studentRepository.findByUserId(myId).orElseThrow(() -> new PeepApiException(ErrorCode.USER_NOT_FOUND));
        Student following = studentRepository.findByUserId(userId).orElseThrow(() -> new PeepApiException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userId)));
        if(!following.getIsDeleted()) {
            Follow follow = Follow.of(follower, following);
            followRepository.save(follow);
        } else {
            throw new PeepApiException(ErrorCode.USER_NOT_FOUND);
        }
        return "Success Following [" + userId + "]";
    }

    public String unFollow(String myId, String userId) {

        followRepository.deleteByFollowerUserIdAndFollowingUserId(myId, userId).orElseThrow(() -> new PeepApiException(ErrorCode.INVALID_PARAMETER));

        return "UnFollow [" + userId + "]";
    }

    public List<StudentResponse> getFollowingList(String myId, String userId, Pageable pageable) {

        return followRepository.findAllByFollowerUserId(userId, pageable)
                .stream()
                .map(follow -> studentRepository.findById(follow.getFollowing().getId())
                        .filter(student -> !student.getIsDeleted())
                        .map(student -> {
                            if(myId.equals(userId) || myId.equals(student.getUserId())) {
                                return StudentResponse.from(student);
                            }
                            return StudentResponse.from(student, isFollowedByMe(myId, student.getUserId()));
                        })
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<StudentResponse> getFollowerList(String myId, String userId, Pageable pageable) {
        return followRepository.findAllByFollowingUserId(userId, pageable)
                .stream()
                .map(follow -> studentRepository.findById(follow.getFollower().getId())
                        .filter(student -> !student.getIsDeleted())
                        .map(student -> {
                            if(myId.equals(student.getUserId())) {
                                return StudentResponse.from(student);
                            }
                            return StudentResponse.from(student, isFollowedByMe(myId, student.getUserId()));
                        }).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public String blockFollow(String myId, String userId) {

        Student blocker = studentRepository.findByUserId(myId).orElseThrow(() -> new PeepApiException(ErrorCode.RESOURCE_NOT_FOUND, ""));
        Student blocked = studentRepository.findByUserId(userId).orElseThrow(() -> new PeepApiException(ErrorCode.RESOURCE_NOT_FOUND, ""));

        Block block = Block.of(blocker, blocked);

        blockRepository.save(block);

        return "Success block [" + userId + "]";
    }

    public String unBlockFollow(String myId, String userId) {

        blockRepository.deleteByBlockerUserIdAndBlockedUserId(myId, userId).orElseThrow(() -> new PeepApiException(ErrorCode.RESOURCE_NOT_FOUND, ""));

        return "Success unblock [" + userId + "]";

    }

    private boolean isFollowedByMe(String myId, String followerId){
        Optional<Follow> follow = followRepository.findByFollowerUserIdAndFollowingUserId(myId, followerId);
        return follow.isPresent();
    }
}
