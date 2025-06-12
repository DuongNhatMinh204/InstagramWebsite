package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/post")
    public ApiResponse getPostOfUserLogin(){
        log.info("getPostOfUserLogin");
        ApiResponse apiResponse = new ApiResponse(profileService.getAllPosts());
        log.info("getPostOfUserLoginSucessfully");
        return apiResponse;
    }

    @PostMapping("/upload-avt")
    public ApiResponse uploadAvatar(@RequestParam String pathUrl){
        log.info("start uploadAvatar");
        ApiResponse apiResponse = new ApiResponse(profileService.upLoadAvt(pathUrl));
        log.info("end uploadAvatar");
        return apiResponse;
    }

    @GetMapping("/followings")
    public ApiResponse getFollowingsOfUserLogin() {
        log.info("getFollowingsOfUserLogin");
        ApiResponse apiResponse = new ApiResponse(profileService.getFollowings());
        log.info("getFollowingsOfUserLoginSuccessfully");
        return apiResponse;
    }
}
