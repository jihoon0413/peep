package com.example.peep.controller;

import com.example.peep.dto.SchoolDto;
import com.example.peep.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/schools")
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/getList")
    public List<SchoolDto> getSchoolList() {
        return schoolService.getSchoolList();
    }

}
