package com.example.peep.controller;

import com.example.peep.dto.request.UserIdRequest;
import com.example.peep.dto.response.Response;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/follow")
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping("/newFollow")
    public Response<String> newFollow(Authentication authentication,
                                          @RequestBody UserIdRequest followRequest
    ) {
        return Response.success(followService.newFollow(authentication.getName(), followRequest.userId()));
    }

    @PostMapping("/unFollow")
    public Response<String> unFollow(Authentication authentication,
                                         @RequestBody UserIdRequest followRequest
    ) {
        return Response.success(followService.unFollow(authentication.getName(), followRequest.userId()));
    }

    @GetMapping("/getFollowingList")
    public Response<List<StudentResponse>> getFollowingList(Authentication authentication,
                                                            @RequestParam("userId") String userId, Pageable pageable) {
        return Response.success(followService.getFollowingList(authentication.getName(), userId, pageable));
    }

    @GetMapping("/getFollowerList")
    public Response<List<StudentResponse>> getFollowerList(Authentication authentication,
                                                                 @RequestParam("userId") String userId, Pageable pageable) {
        return Response.success(followService.getFollowerList(authentication.getName(), userId, pageable));
    }

    @PostMapping("/block")
    public Response<String> blockFollow(Authentication authentication,
                                         @RequestBody UserIdRequest followRequest) {
        return Response.success(followService.blockFollow(authentication.getName(), followRequest.userId()));
    }

    @PostMapping("/unBlock")
    public Response<String> unBlockFollow(Authentication authentication,
                                           @RequestBody UserIdRequest followRequest) {
        return Response.success(followService.unBlockFollow(authentication.getName(), followRequest.userId()));
    }
}
