package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.model.request.ProfileRequestDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
//import com.nminh.websiteinstagram.service.ImageServiceimageService;
import com.nminh.websiteinstagram.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/profile")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;
//    private final ImageServiceimageService  imageServiceimageService;


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
    @PutMapping("/update")
    public ApiResponse updateProfile(@RequestBody ProfileRequestDTO requestDTO) {
        log.info("start updateProfile");
        ApiResponse apiResponse = new ApiResponse(profileService.updateProfile(requestDTO));
        log.info("end updateProfile successfully");
        return apiResponse;
    }
    @GetMapping("/posts/user/{userId}")
    public ApiResponse getPostsByUserId(@PathVariable Long userId){
        log.info("getPostsByUserId for userId: {}", userId);
        ApiResponse apiResponse = new ApiResponse(profileService.getPostsOfUser(userId));
        log.info("getPostsByUserIdSuccessfully for userId: {}", userId);
        return apiResponse;
    }

}
