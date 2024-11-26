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

        Long schoolId = studentDto.schoolDto().id();

        School school = schoolRepository.getReferenceById(schoolId);
        Coin coin = Coin.of(0);
        Photo photo = Photo.of(null);
        Student student = studentDto.toEntity(school, coin, photo);

        String hashedPassword = passwordEncoder.encode(student.getUserPassword());
        student.setUserPassword(hashedPassword);

        coinRepository.save(coin);
        photoRepository.save(photo);
        studentRepository.save(student);
    }

    public String getStudent(String userId) {
        Student student = studentRepository.findById(1L).orElse(null);
        return student.getUserId();
    }


}
