package com.example.peep.controller;

import com.example.peep.dto.request.FollowRequest;
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

    @PostMapping("/newFollow")
    public ResponseEntity<Void> newFollow(@RequestHeader("Authorization") String token,
                                    @RequestBody FollowRequest followRequest
    ) {
        return followService.newFollow(token, followRequest.userId());
    }

    @PostMapping("/unFollow")
    public ResponseEntity<Void> unFollow(@RequestHeader("Authorization") String token,
                                         @RequestBody FollowRequest followRequest
    ) {
        return followService.unFollow(token, followRequest.userId());
    }

    @GetMapping("/getFollowingList")
    public ResponseEntity<List<StudentResponse>> getFollowingList(@RequestHeader("Authorization") String token,
                                                                  @RequestParam("userId") String userId) {
        return followService.getFollowingList(token, userId);
    }

    @GetMapping("/getFollowerList")
    public ResponseEntity<List<StudentResponse>> getFollowerList(@RequestHeader("Authorization") String token,
                                                                 @RequestParam("userId") String userId) {
        return followService.getFollowerList(token, userId);
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockFollow(@RequestHeader("Authorization") String token,
                                         @RequestParam("userId") String userId) {
        return followService.blockFollow(token, userId);
    }

    @PostMapping("/unBlock")
    public ResponseEntity<?> unBlockFollow(@RequestHeader("Authorization") String token,
                                           @RequestParam("userId") String userId) {
        return followService.unBlockFollow(token, userId);
    }
}
