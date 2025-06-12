package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.constant.Constants;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.model.response.JwtResponse;
import com.nminh.websiteinstagram.model.response.UserLoginResponseDTO;
import com.nminh.websiteinstagram.security.CustomUserDetails;
import com.nminh.websiteinstagram.security.CustomUserDetailsService;
import com.nminh.websiteinstagram.security.JWTService;
import com.nminh.websiteinstagram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private  UserService userService;

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JWTService jwtService;

    @PostMapping("/register")
    public ApiResponse registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("Registering user: {}", userRegisterDTO);
        ApiResponse apiResponse = new ApiResponse(Constants.SUCCESS, userService.createUser(userRegisterDTO));
        log.info("Registered user: {}", apiResponse);
        return apiResponse;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        log.info("Logining user: {}", userLoginDTO);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getPhone(), userLoginDTO.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDTO.getPhone());
        String token = jwtService.generateToken(userDetails);

        log.info("Logged in user: {}", userLoginDTO);
        return ResponseEntity.ok(new JwtResponse(token));
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // Lấy thông tin xác thực từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == "anonymousUser") {
            return ResponseEntity.status(401).body("Chưa đăng nhập hoặc token không hợp lệ");
        }

        // Lấy thông tin người dùng từ CustomUserDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String phone = userDetails.getUsername();

        // Trả về thông tin người dùng
        return ResponseEntity.ok(new UserLoginResponseDTO(userId, phone));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        return userService.getInfo() ;
    }

}
