package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import org.springframework.stereotype.Service;


public interface UserService {
    public User createUser(UserRegisterDTO userRegisterDTO);
    public User loginUser(UserLoginDTO userLoginDTO);
}
