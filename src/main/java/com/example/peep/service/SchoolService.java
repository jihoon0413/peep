package com.example.peep.service;

import com.example.peep.dto.SchoolDto;
import com.example.peep.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public ResponseEntity<List<SchoolDto>> getSchoolList() {
        return ResponseEntity.ok(schoolRepository.findAll()
                .stream().map(SchoolDto::from).toList());
    }
}
