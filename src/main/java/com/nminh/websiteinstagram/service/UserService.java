package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.UserInfoResponse;
import com.nminh.websiteinstagram.model.response.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    public User createUser(UserRegisterDTO userRegisterDTO);
    public User loginUser(UserLoginDTO userLoginDTO);

    ResponseEntity<?> getInfo();

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<UserInfoResponse> allUsers();

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    UserInfoResponse changeStatus(Long id);
}
