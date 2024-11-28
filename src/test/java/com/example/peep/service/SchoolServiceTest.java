package com.example.peep.service;

import com.example.peep.domain.School;
import com.example.peep.dto.SchoolDto;
import com.example.peep.repository.SchoolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    @Mock private SchoolRepository schoolRepository;

    @InjectMocks
    private SchoolService schoolService;


    @Test
    void given_when_then() {

        //Given
        List<School> givenList = new ArrayList<>();
        givenList.add(School.of("광주광역시교육청", "광주고등학교"));
        givenList.add(School.of("광주광역시교육청", "광주제일고등학교"));

        //When
        given(schoolRepository.findAll()).willReturn(givenList);

        List<SchoolDto> schools = schoolService.getSchoolList();
        //Then
        assertThat(schools.size()).isEqualTo(2);
        then(schoolRepository).should().findAll();
    }

}