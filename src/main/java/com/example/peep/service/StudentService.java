package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.*;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public void newStudent(StudentDto studentDto) {

        School school = schoolRepository.getReferenceById(studentDto.schoolDto().id());
        Coin coin = Coin.of(0);
        Photo photo = Photo.of(null);
        Student student = studentDto.toEntity(school, coin, photo);

        String hashedPassword = passwordEncoder.encode(student.getUserPassword());
        student.setUserPassword(hashedPassword);

        coinRepository.save(coin);
        photoRepository.save(photo);
        studentRepository.save(student);
    }

    public void modifyStudent(String accessToken, StudentDto studentDto) {
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
    }

    public void deleteStudent(String token) {

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
    }

    public StudentResponse getStudent(String userId) {
        Student student = studentRepository.findByUserId(userId).orElseThrow();
        return StudentResponse.from(student);
    }
}
