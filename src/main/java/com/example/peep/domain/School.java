package com.example.peep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class School extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column private String educationOffice;
    @Setter @Column private String schoolName;

    private School(String educationOffice, String schoolName) {
        this.educationOffice = educationOffice;
        this.schoolName = schoolName;
    }

    public static School of(String educationOffice, String schoolName) {
        return new School(educationOffice, schoolName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof School school)) return false;
        return Objects.equals(getId(), school.getId()) && Objects.equals(getSchoolName(), school.getSchoolName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSchoolName());
    }
}
