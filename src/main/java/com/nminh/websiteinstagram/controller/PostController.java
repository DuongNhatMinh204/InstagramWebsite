package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.constant.Constants;
import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.model.request.PostCreateDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.security.CustomUserDetails;
import com.nminh.websiteinstagram.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user/post")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    // tạo bài đăng
    @PostMapping("/create")
    public ApiResponse createPost(@RequestBody PostCreateDTO postCreateDTO) {
        log.info("Post Create Request: {}", postCreateDTO);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(postService.createPost(postCreateDTO));
        apiResponse.setMessage(Constants.SUCCESS);

        return apiResponse;
    }
    // lấy danh sách từ danh sách người mình follow
    @GetMapping("/getpost")
   public ApiResponse getPost(){
        log.info("Get post from people you follow") ;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(postService.getAllPostsFromFollower());
        apiResponse.setMessage(Constants.SUCCESS);
        return apiResponse;
    }

    /**
     * api ấy post theo id
     * @param id
     * @return
     */
    @GetMapping("/post/{id}")
    public ApiResponse getPostById(@PathVariable Long id){
        log.info("Get post from people you follow") ;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(postService.getPostById(id));
        return apiResponse;
    }

    /**
     * api lấy danh sách post theo id
     * @param userId
     * @return
     */
    @GetMapping("/post-list/{userId}")
    public ApiResponse getPostList(@RequestParam Long userId){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(postService.getAllPostByUserId(userId));
        log.info("get lít post by userId : {}", apiResponse);
        return apiResponse;
    }

}
