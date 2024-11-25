package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.*;
import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final CoinRepository coinRepository;
    private final PhotoRepository photoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

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
        System.out.println(userId);
        Student student = studentRepository.findById(1L).orElse(null);
        return student.getUserId();
    }

    public JwtTokenDto login(String deviceId, StudentDto studentDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(studentDto.userId(), studentDto.userPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return JwtTokenDto.from(jwtTokenProvider.generateToken(authentication, studentDto.userId(), deviceId));

    }

    public JwtTokenDto refreshToken(String deviceId, String userId, JwtTokenDto jwtTokenDto) {

        RefreshToken refresh = refreshTokenRepository.findByUserIdAndDeviceId(userId, deviceId);

        String accessToken = "";

        try {
            if (jwtTokenProvider.validateToken(jwtTokenDto.refreshToken()) && jwtTokenDto.refreshToken().contains(refresh.getToken())) {
                accessToken = jwtTokenProvider.generateAccess(userId);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            log.info("Invalid token");
        }
        JwtToken jwtToken = new JwtToken("Bearer", accessToken, refresh.getToken(), userId);

        return JwtTokenDto.from(jwtToken);
    }
}
