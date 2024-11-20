package com.example.peep.dto;

import com.example.peep.domain.School;

public record SchoolDto(
        Long id,
        String educationOffice,
        String schoolName
) {

    public static SchoolDto of(Long id, String educationOffice, String schoolName) {
        return new SchoolDto(id,educationOffice, schoolName);
    }

    public static SchoolDto from(School school) {
        return new SchoolDto (
                school.getId(),
                school.getEducationOffice(),
                school.getSchoolName()
        );
    }

    public School toEntity() {
        return School.of(
                educationOffice,schoolName
        );
    }


}
