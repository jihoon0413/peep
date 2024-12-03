package com.example.peep.repository;

import com.example.peep.domain.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {
}
