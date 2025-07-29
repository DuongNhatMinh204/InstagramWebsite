package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.ProfileDTO;
import org.springframework.http.ResponseEntity;


public interface UserService {
    public User createUser(UserRegisterDTO userRegisterDTO);
    public User loginUser(UserLoginDTO userLoginDTO);

    ResponseEntity<?> getInfo();

    ProfileDTO findUserById(Long userId);
}
