package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@Slf4j
public class LikeController {
    @Autowired
    private LikeService likeService;
    @PostMapping("/likepost")
    public ApiResponse likePost(@RequestParam  Long postId) {
        log.info("Like post id: " + postId);
        ApiResponse apiResponse = new ApiResponse(likeService.likePost(postId));
        log.info("Liked post id: " + postId );
        return apiResponse;
    }
    @DeleteMapping("/unlike")
    public ApiResponse unlikePost(@RequestParam  Long postId) {
        log.info("Unlike post id: " + postId );
        ApiResponse apiResponse = new ApiResponse(likeService.unlikePost(postId));
        log.info("Unliked post id: " + postId );
        return apiResponse;
    }
    @GetMapping("/check-liked")
    public ApiResponse checkLiked(@RequestParam Long postId) {
        boolean liked = likeService.hasUserLikedPost(postId);
        return new ApiResponse(liked);
    }
    @GetMapping("/count-liked")
    public ApiResponse countLiked(@RequestParam Long postId) {
        log.info("Count liked posts");
        return new ApiResponse(likeService.countLike(postId));
    }
}
