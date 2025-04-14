package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.constant.Constants;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private  UserService userService;

    @PostMapping("/register")
    public ApiResponse registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("Registering user: {}", userRegisterDTO);
        ApiResponse apiResponse = new ApiResponse(Constants.SUCCESS, userService.createUser(userRegisterDTO));
        log.info("Registered user: {}", apiResponse);
        return apiResponse;
    }

    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        log.info("Logining user: {}", userLoginDTO);
        ApiResponse apiResponse = new ApiResponse(Constants.SUCCESS , userService.loginUser(userLoginDTO));
        log.info("Logined user: {}", apiResponse);
        return apiResponse;
    }
}
