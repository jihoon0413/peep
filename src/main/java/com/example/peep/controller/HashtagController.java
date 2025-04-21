package com.example.peep.controller;

import com.example.peep.domain.enumType.HashtagType;
import com.example.peep.dto.HashtagDto;
import com.example.peep.dto.response.Response;
import com.example.peep.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/hashtag")
@RestController
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping("/getHashList")
    public Response<List<HashtagDto>> getHashList(@RequestParam("type") String type) {
        return Response.success(hashtagService.getHashList(HashtagType.valueOf(type)));
    }

    @GetMapping("/getMyHashtag")
    public Response<List<HashtagDto>> getMyHashtag(Authentication authentication) {
        return Response.success(hashtagService.getMyHashtag(authentication.getName()));
    }

    @PostMapping("/setMyHashtag")
    public Response<String> setMyHashtag(
            Authentication authentication,
            @RequestBody List<HashtagDto> hashtagDtoList) {
        return Response.success(hashtagService.setMyHashtag(authentication.getName(), hashtagDtoList));
    }

    @PostMapping("/deleteMyHashtag")
    public Response<String> deleteMyHashtag(
            Authentication authentication,
            @RequestBody List<HashtagDto> hashtagDtoList) {
        return Response.success(hashtagService.deleteMyHashtag(authentication.getName(), hashtagDtoList));
    }

}
