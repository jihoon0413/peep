package com.example.peep.service;

import com.example.peep.domain.*;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final CoinRepository coinRepository;
    private final PhotoRepository photoRepository;
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

    public StudentDto getStudent(String userId) {
        Student student = studentRepository.findByUserId(userId).orElseThrow();
        return StudentDto.from(student);
    }


    public void modifyStudent(StudentDto studentDto) {
        Student student = studentRepository.findByUserId(studentDto.userId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 아이디입니다."));
        Photo photo = photoRepository.findById(student.getPhoto().getId()).orElseThrow();
        School school = schoolRepository.findById(studentDto.schoolDto().id()).orElseThrow();

        photo.setPhotoUrl(studentDto.photoDto().photoUrl());

        student.setUserId(studentDto.userId());
        student.setUserPassword(studentDto.userPassword());
        student.setName(studentDto.name());
        student.setSchool(school);
        student.setPhoto(photo);
        student.setGrade(studentDto.grade());
        student.setMyClass(studentDto.myClass());
        student.setTel(studentDto.tel());

        studentRepository.save(student);
    }
}
