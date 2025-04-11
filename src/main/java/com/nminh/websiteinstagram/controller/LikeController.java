package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@Slf4j
public class LikeController {
    @Autowired
    private LikeService likeService;
    @PostMapping("/likepost")
    public ApiResponse likePost(@RequestParam  Long postId, @RequestParam  Long userId) {
        log.info("Like post id: " + postId + " userId: " + userId);
        ApiResponse apiResponse = new ApiResponse(likeService.likePost(postId, userId));
        log.info("Liked post id: " + postId + " userId: " + userId);
        return apiResponse;
    }
    @DeleteMapping("/unlike")
    public ApiResponse unlikePost(@RequestParam  Long postId, @RequestParam  Long userId) {
        log.info("Unlike post id: " + postId + " userId: " + userId);
        ApiResponse apiResponse = new ApiResponse(likeService.unlikePost(postId, userId));
        log.info("Unliked post id: " + postId + " userId: " + userId);
        return apiResponse;
    }
}
