package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.*;
import com.example.peep.domain.mapping.StudentCommunity;
import com.example.peep.domain.mapping.StudentHashtag;
import com.example.peep.dto.HashtagDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentDetailResponse;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final CoinRepository coinRepository;
    private final PhotoRepository photoRepository;
    private final FollowRepository followRepository;
    private final StudentHashtagRepository studentHashtagRepository;
    private final StudentCommunityRepository studentCommunityRepository;
    private final CommunityRepository communityRepository;
    private final HashtagRepository hashtagRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public String newStudent(StudentDto studentDto) {

        School school = schoolRepository.getReferenceById(studentDto.schoolDto().id());
        Coin coin = Coin.of(0);
        Photo photo = Photo.of(studentDto.photoDto().photoUrl());
        Student student = studentDto.toEntity(school, coin, photo);

        String hashedPassword = passwordEncoder.encode(student.getUserPassword());
        student.setUserPassword(hashedPassword);

        coinRepository.save(coin);
        photoRepository.save(photo);
        studentRepository.save(student);

        if(studentDto.hashtagDtos() != null) {
            for (HashtagDto hashtagDto : studentDto.hashtagDtos()) {
                StudentHashtag studentHashtag = studentHashtagRepository.findByStudentUserIdAndHashtagId(studentDto.userId(), hashtagDto.id()).orElse(null);
                if (studentHashtag == null) {
                    Hashtag hashtag = hashtagRepository.findById(hashtagDto.id()).orElseThrow();
                    studentHashtagRepository.save(StudentHashtag.of(student, hashtag));
                }
            }
        }

        Community community = communityRepository.findBySchoolIdAndGradeAndMyClass(school.getId(),0,0).orElse(null);

        if(community == null) {
            community = Community.of(school);
            communityRepository.save(community);
        }

        StudentCommunity studentCommunity = StudentCommunity.of(student, community);
        studentCommunityRepository.save(studentCommunity);

        return "Success create new Student. Welcome to join us";
    }

    public String modifyStudent(String myId, StudentDto studentDto) {
        Student student = studentRepository.findByUserId(myId).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 아이디입니다."));
        Photo photo = photoRepository.findById(student.getPhoto().getId()).orElseThrow();
        School school = schoolRepository.findById(studentDto.schoolDto().id()).orElseThrow();
        String hashedPassword = passwordEncoder.encode(studentDto.userPassword());

        photo.setPhotoUrl(studentDto.photoDto().photoUrl());

        student.setUserId(studentDto.userId());
        student.setUserPassword(hashedPassword);
        student.setName(studentDto.name());
        student.setSchool(school);
        student.setPhoto(photo);
        student.setGrade(studentDto.grade());
        student.setMyClass(studentDto.myClass());
        student.setTel(studentDto.tel());

        studentRepository.save(student);

        return "Success modify your info";
    }

    public String deleteStudent(String myId) {

        Student student = studentRepository.findByUserId(myId).orElseThrow();

        followRepository.softDeleteAllByUserId(student.getId());

        student.setIsDeleted(true);
        student.getCoin().setIsDeleted(true);
        student.getPhoto().setIsDeleted(true);

        studentRepository.save(student);

        return "Success delete [" + myId+"]";
    }

    public List<StudentResponse> getFourStudents(String myId, Long communityId) {

        return studentRepository.findRandomFourStudents(communityId, myId)
                .stream().map(StudentResponse::from)
                .toList();
    }

    public StudentDetailResponse getStudent(String myId, String userId) {

        Student student = studentRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Resource not exists"));

        int followerCount = followRepository.countByFollowingUserId(student.getId());
        int followingCount = followRepository.countByFollowerUserId(student.getId());
        List<HashtagDto> hashtagDtoList = student.getHashtags().stream()
                .map(studentHashtag -> HashtagDto.from(studentHashtag.getHashtag()))
                .toList();
        Boolean isFollowedByMe = null;
        if(!myId.equals(userId)) {
            isFollowedByMe = isFollowedByMe(myId, userId);
        }

        return StudentDetailResponse.from(student, isFollowedByMe, followerCount, followingCount, hashtagDtoList);
    }

    private boolean isFollowedByMe(String myId, String followerId){
        Optional<Follow> follow = followRepository.findByFollowerUserIdAndFollowingUserId(myId, followerId);
        return follow.isPresent();
    }

    public boolean isDuplicated(String userId) {
        Student student = studentRepository.findByUserId(userId).orElse(null);

        return student != null;
    }
}
