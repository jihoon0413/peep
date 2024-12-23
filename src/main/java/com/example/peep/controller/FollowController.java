package com.example.peep.controller;

import com.example.peep.dto.response.StudentResponse;
import com.example.peep.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/follow")
@RestController
public class FollowController {

    private final FollowService followService;

    @GetMapping("/newFollow")
    public ResponseEntity<Void> newFollow(@RequestHeader("Authorization") String token,
                                    @RequestParam("userId") String userId
    ) {
        return followService.newFollow(token, userId);
    }

    @GetMapping("/unFollow")
    public ResponseEntity<Void> unFollow(@RequestHeader("Authorization") String token,
                         @RequestParam("userId") String userId
    ) {
        return followService.unFollow(token, userId);
    }

    @GetMapping("/getFollowingList")
    public ResponseEntity<List<StudentResponse>> getFollowingList(@RequestParam("userId") String userId) {
        return followService.getFollowingList(userId);
    }

    @GetMapping("/getFollowerList")
    public ResponseEntity<List<StudentResponse>> getFollowerList(@RequestParam("userId") String userId) {
        return followService.getFollowerList(userId);
    }

}
