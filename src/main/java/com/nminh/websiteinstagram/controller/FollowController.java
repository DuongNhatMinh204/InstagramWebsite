package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user/follow")
@Slf4j
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/following")
    public ApiResponse followTo(@RequestParam Long followingUserId) {
        log.info("Following user  following {}",  followingUserId);
        ApiResponse apiResponse = new ApiResponse(followService.follow(followingUserId));
        log.info("Following user  successfully followed {}", followingUserId);
        return apiResponse;
    }

    @DeleteMapping("/unfollowing")
    public ApiResponse unfollowFrom(@RequestParam Long unfollowingUserId) {
        log.info("Unfollowing user  following {}",  unfollowingUserId);
        ApiResponse apiResponse = new ApiResponse(followService.unfollow(unfollowingUserId));
        log.info("Unfollowing user  successfully unfollowed {}",  unfollowingUserId);
        return apiResponse;
    }

    // xem danh sách người theo dõi mình
    @GetMapping("/listfollower")
    public ApiResponse listFollower() {
        log.info("List follower getting ");
        ApiResponse apiResponse = new ApiResponse(followService.getFollowers());
        log.info("Following user  successfully listFollower");
        return apiResponse;
    }

    //xem danh sách người mình theo dõi
    @GetMapping("/listfollowing")
    public ApiResponse listFollowing() {
        log.info("Following user  listFollowing");
        ApiResponse apiResponse = new ApiResponse(followService.getFollowing());
        log.info("Following user successfully listFollowing");
        return apiResponse;
    }

    @GetMapping("/check-follow")
    public Boolean checkFollow(@RequestParam Long followingUserId) {
        log.info("Check following user  following {}",  followingUserId);
        return followService.checkFollowing(followingUserId);
    }
}
