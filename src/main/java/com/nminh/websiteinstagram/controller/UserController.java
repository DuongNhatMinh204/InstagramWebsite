package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.constant.Constants;
import com.nminh.websiteinstagram.entity.BlacklistedToken;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.model.response.JwtResponse;
import com.nminh.websiteinstagram.model.response.UserLoginResponseDTO;
import com.nminh.websiteinstagram.repository.BlacklistedTokenRepository;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.security.CustomUserDetails;
import com.nminh.websiteinstagram.security.CustomUserDetailsService;
import com.nminh.websiteinstagram.security.JWTService;
import com.nminh.websiteinstagram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Base64;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping("/register")
    public ApiResponse registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("Registering user: {}", userRegisterDTO);
        ApiResponse apiResponse = new ApiResponse(Constants.SUCCESS, userService.createUser(userRegisterDTO));
        log.info("Registered user: {}", apiResponse);
        return apiResponse;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        User user = userRepository.findByPhone(userLoginDTO.getPhone()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if(user.getStatus() == 0){
            throw new AppException(ErrorCode.ACCOUNT_LOCKET);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getPhone(), userLoginDTO.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDTO.getPhone());
        String token = jwtService.generateToken(userDetails);

        log.info("Logged in user: {}", userLoginDTO);

        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getBytes());
        log.info("base64Secret: {}", base64Secret);

        return ResponseEntity.ok(new JwtResponse(token,user.getRole()));
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

        var auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.info("authorities: {}", auth);

        // Trả về thông tin người dùng
        return ResponseEntity.ok(new UserLoginResponseDTO(userId, phone));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        return userService.getInfo() ;
    }

    @GetMapping("/all-user")
    public ApiResponse getAllUser() {
        log.info("Admin start get all user");
        ApiResponse apiResponse = new ApiResponse(userService.allUsers());
        log.info("Admin end get all user");
        return apiResponse;
    }

    @PutMapping("/change-status/{id}")
    public ApiResponse changeStatus(@PathVariable("id") Long id){
        log.info("Admin start change status of user: {}", id);
        ApiResponse apiResponse = new ApiResponse(userService.changeStatus(id));
        log.info("Admin end change status of user: {}", apiResponse);
        return apiResponse;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token không hợp lệ");
        }
        String token = authHeader.substring(7);
        // Lưu token vào blacklist
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setBlacklistedAt(LocalDateTime.now());
        blacklistedTokenRepository.save(blacklistedToken);
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}
