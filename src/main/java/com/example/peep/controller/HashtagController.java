package com.example.peep.controller;

import com.example.peep.dto.HashtagDto;
import com.example.peep.enumType.HashtagType;
import com.example.peep.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/hashtag")
@RestController
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping("/getHashList")
    public List<HashtagDto> getHashList(@RequestParam("type") String type) {
        return hashtagService.getHashList(HashtagType.valueOf(type));
    }

    @GetMapping("/getMyHashtag")
    public List<HashtagDto> getMyHashtag(@RequestHeader("Authorization") String token) {
        return hashtagService.getMyHashtag(token);
    }

    @PostMapping("/setMyHashtag")
    public void setMyHashtag(
            @RequestHeader("Authorization") String token,
            @RequestBody List<HashtagDto> hashtagDtoList) {
        hashtagService.setMyHashtag(token, hashtagDtoList);
    }

    @PostMapping("/deleteMyHashtag")
    public void deleteMyHashtag(
            @RequestHeader("Authorization") String token,
            @RequestBody List<HashtagDto> hashtagDtoList) {
        hashtagService.deleteMyHashtag(token, hashtagDtoList);
    }

}