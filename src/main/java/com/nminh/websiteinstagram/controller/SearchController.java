package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.model.response.SearchByNickNameDto;
import com.nminh.websiteinstagram.model.response.UserSuggestionDto;
import com.nminh.websiteinstagram.security.CustomUserDetails;
import com.nminh.websiteinstagram.security.CustomUserDetailsService;
import com.nminh.websiteinstagram.security.JWTService;
import com.nminh.websiteinstagram.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/user/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    @Autowired
    private UserService userService;

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JWTService jwtService;
    @GetMapping("/search")
    public ResponseEntity<List<SearchByNickNameDto>> searchUsers(@RequestParam String nickName) {
        List<SearchByNickNameDto> users = userService.searchUsersByNickname(nickName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/suggested")
    public ResponseEntity<List<UserSuggestionDto>> getUnfollowedSuggestions(
            @RequestParam(defaultValue = "5") int limit,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Người dùng chưa đăng nhập");
        }
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("Xác thực người dùng không hợp lệ");
        }
        Long currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(userService.getUnfollowedSuggestions(currentUserId, limit));
    }
}