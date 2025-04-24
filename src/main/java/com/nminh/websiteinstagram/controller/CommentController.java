package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.entity.Comment;
import com.nminh.websiteinstagram.model.request.CommentCreateDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user/cmt")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/add")
    public ApiResponse addComment( @RequestParam Long postId, @RequestBody CommentCreateDTO commentCreateDTO) {
        log.info("addComment for post id : {} ", postId);
        ApiResponse apiResponse = new ApiResponse(commentService.addComment( postId, commentCreateDTO));
        log.info("addComment apiResponse : {}", apiResponse);
        return apiResponse;
    }
}
