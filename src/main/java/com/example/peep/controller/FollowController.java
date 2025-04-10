package com.example.peep.controller;

import com.example.peep.dto.request.FollowRequest;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/follow")
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping("/newFollow")
    public ResponseEntity<Void> newFollow(Authentication authentication,
                                          @RequestBody FollowRequest followRequest
    ) {
        return followService.newFollow(authentication.getName(), followRequest.userId());
    }

    @PostMapping("/unFollow")
    public ResponseEntity<Void> unFollow(Authentication authentication,
                                         @RequestBody FollowRequest followRequest
    ) {
        return followService.unFollow(authentication.getName(), followRequest.userId());
    }

    @GetMapping("/getFollowingList")
    public ResponseEntity<List<StudentResponse>> getFollowingList(Authentication authentication,
                                                                  @RequestParam("userId") String userId) {
        return followService.getFollowingList(authentication.getName(), userId);
    }

    @GetMapping("/getFollowerList")
    public ResponseEntity<List<StudentResponse>> getFollowerList(Authentication authentication,
                                                                 @RequestParam("userId") String userId) {
        return followService.getFollowerList(authentication.getName(), userId);
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockFollow(Authentication authentication,
                                         @RequestBody FollowRequest followRequest) {
        return followService.blockFollow(authentication.getName(), followRequest.userId());
    }

    @PostMapping("/unBlock")
    public ResponseEntity<?> unBlockFollow(Authentication authentication,
                                           @RequestBody FollowRequest followRequest) {
        return followService.unBlockFollow(authentication.getName(), followRequest.userId());
    }
}
