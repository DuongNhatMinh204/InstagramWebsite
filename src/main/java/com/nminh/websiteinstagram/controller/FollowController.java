package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/follow")
@Slf4j
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/following")
    public ApiResponse followTo(@RequestParam Long userId, @RequestParam Long followingUserId) {
        log.info("Following user {} following {}", userId, followingUserId);
        ApiResponse apiResponse = new ApiResponse(followService.follow(userId, followingUserId));
        log.info("Following user {} successfully followed {}", userId, followingUserId);
        return apiResponse;
    }

    @DeleteMapping("/unfollowing")
    public ApiResponse unfollowFrom(@RequestParam Long userId, @RequestParam Long unfollowingUserId) {
        log.info("Unfollowing user {} following {}", userId, unfollowingUserId);
        ApiResponse apiResponse = new ApiResponse(followService.unfollow(userId, unfollowingUserId));
        log.info("Unfollowing user {} successfully unfollowed {}", userId, unfollowingUserId);
        return apiResponse;
    }

    // xem danh sách người theo dõi mình
    @GetMapping("/listfollower")
    public ApiResponse listFollower(@RequestParam Long userId) {
        log.info("Following user {} listFollower", userId);
        ApiResponse apiResponse = new ApiResponse(followService.getFollowers(userId));
        log.info("Following user {} successfully listFollower", userId);
        return apiResponse;
    }

    //xem danh sách người mình theo dõi
    @GetMapping("/listfollowing")
    public ApiResponse listFollowing(@RequestParam Long userId) {
        log.info("Following user {} listFollowing", userId);
        ApiResponse apiResponse = new ApiResponse(followService.getFollowing(userId));
        log.info("Following user {} successfully listFollowing", userId);
        return apiResponse;
    }
}
