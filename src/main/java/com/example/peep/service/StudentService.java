package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.*;
import com.example.peep.domain.mapping.StudentCommunity;
import com.example.peep.domain.mapping.StudentHashtag;
import com.example.peep.dto.HashtagDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.errors.errorcode.CommonErrorCode;
import com.example.peep.errors.errorcode.CustomErrorCode;
import com.example.peep.errors.exception.RestApiException;
import com.example.peep.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ResponseEntity<Void> newStudent(StudentDto studentDto) {

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

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> modifyStudent(String accessToken, StudentDto studentDto) {
        String myId = jwtTokenProvider.getUserId(accessToken);
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

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteStudent(String token) {

        String myId = jwtTokenProvider.getUserId(token);

        Student student = studentRepository.findByUserId(myId).orElseThrow();

        followRepository.findAllByFollowerUserId(student.getUserId())
                .forEach(follow -> {
                    follow.setIsDeleted(true);
                    followRepository.save(follow);
                });

        followRepository.findAllByFollowingUserId(student.getUserId())
                .forEach(follow -> {
                    follow.setIsDeleted(true);
                    followRepository.save(follow);
                });

        student.setIsDeleted(true);
        student.getCoin().setIsDeleted(true);
        student.getPhoto().setIsDeleted(true);

        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<StudentResponse>> getFourStudents(String token, Long communityId) {
        String myId = jwtTokenProvider.getUserId(token);

        return ResponseEntity.ok(studentRepository.findRandomFourStudents(communityId, myId)
                .stream().map(StudentResponse::from)
                .toList());
    }

    public ResponseEntity<StudentResponse> getStudent(String userId) {
        Student student = studentRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Resource not exists"));
        return ResponseEntity.ok(StudentResponse.from(student));
    }
}
