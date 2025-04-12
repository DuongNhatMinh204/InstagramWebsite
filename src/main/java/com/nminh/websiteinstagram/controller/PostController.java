package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.model.request.PostCreateDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/post")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    // tạo bài đăng
    @PostMapping("/create/{id}")
    public ApiResponse createPost(@PathVariable Long id,@RequestBody PostCreateDTO postCreateDTO) {
        log.info("Post of  user id is {}", id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(postService.createPost(id, postCreateDTO));
        apiResponse.setMessage("Success");
        return apiResponse;
    }
    // lấy danh sách từ danh sách người mình follow
    @GetMapping("/getpost")
   public ApiResponse getPost(@RequestParam Long userId){
        log.info("Get post from people you follow {}", userId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(postService.getAllPostsFromFollower(userId));
        apiResponse.setMessage("Success");
        return apiResponse;
    }

}
