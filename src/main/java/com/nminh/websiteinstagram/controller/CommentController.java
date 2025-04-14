package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.entity.Comment;
import com.nminh.websiteinstagram.model.request.CommentCreateDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cmt")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/add")
    public ApiResponse addComment(@RequestParam Long userId , @RequestParam Long postId, @RequestBody CommentCreateDTO commentCreateDTO) {
        log.info("addComment for post id : {} of user id : {}", postId, userId);
        ApiResponse apiResponse = new ApiResponse(commentService.addComment(userId, postId, commentCreateDTO));
        log.info("addComment apiResponse : {}", apiResponse);
        return apiResponse;
    }
}
