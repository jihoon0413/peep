package com.example.peep.controller;

import com.example.peep.dto.HashtagDto;
import com.example.peep.domain.enumType.HashtagType;
import com.example.peep.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/hashtag")
@RestController
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping("/getHashList")
    public ResponseEntity<List<HashtagDto>> getHashList(@RequestParam("type") String type) {
        return hashtagService.getHashList(HashtagType.valueOf(type));
    }

    @GetMapping("/getMyHashtag")
    public ResponseEntity<List<HashtagDto>> getMyHashtag(Authentication authentication) {
        return hashtagService.getMyHashtag(authentication.getName());
    }

    @PostMapping("/setMyHashtag")
    public ResponseEntity<Void> setMyHashtag(
            Authentication authentication,
            @RequestBody List<HashtagDto> hashtagDtoList) {
        return hashtagService.setMyHashtag(authentication.getName(), hashtagDtoList);
    }

    @PostMapping("/deleteMyHashtag")
    public ResponseEntity<Void> deleteMyHashtag(
            Authentication authentication,
            @RequestBody List<HashtagDto> hashtagDtoList) {
        return hashtagService.deleteMyHashtag(authentication.getName(), hashtagDtoList);
    }

}
