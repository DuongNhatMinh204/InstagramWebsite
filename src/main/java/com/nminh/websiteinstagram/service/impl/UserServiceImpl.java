package com.nminh.websiteinstagram.service.impl;


import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.mapper.UserMapper;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.UserInfoResponse;

import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User createUser(UserRegisterDTO userRegisterDTO) {

        if(userRepository.existsByPhone(userRegisterDTO.getPhone())){
            throw new AppException(ErrorCode.USER_EXISTS) ;
        }

        if(!userRegisterDTO.getPassword().equals(userRegisterDTO.getPasswordConfirm())){
            throw new AppException(ErrorCode.PASSSWORD_MISMATCH) ;
        }

        // mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        userRegisterDTO.setPassword(encodedPassword);

        User user = userMapper.toUser(userRegisterDTO);
        user.setStatus(1);

        return userRepository.save(user);

    }

    @Override
    public User loginUser(UserLoginDTO userLoginDTO) {
        if(!userRepository.existsByPhone(userLoginDTO.getPhone())){
            throw new AppException(ErrorCode.USER_NOT_EXISTS) ;
        }
        User user = userRepository.findByPhone(userLoginDTO.getPhone())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)) ;
        if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.PASSSWORD_MISMATCH) ;
        }

        return user;
    }

    @Override
    public ResponseEntity<?> getInfo() {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)) ;
        return ResponseEntity.ok(user);
    }

    @Override
    public List<UserInfoResponse> allUsers() {
        List<User> users = userRepository.findAll();
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        for(User user : users){
            UserInfoResponse userInfoResponse = new UserInfoResponse();

            userInfoResponse.setPhone(user.getPhone());
            userInfoResponse.setEmail(user.getEmail());
            userInfoResponse.setId(user.getId());
            userInfoResponse.setFullName(user.getFullName());
            userInfoResponse.setStatus(user.getStatus());
            userInfoResponse.setBirthday(user.getBirthday());
            userInfoResponse.setNickName(user.getNickName());
            userInfoResponse.setAvatarUrl(user.getAvatarUrl());
            userInfoResponse.setGender(user.getGender());

            userInfoResponses.add(userInfoResponse);
        }
        return userInfoResponses;
    }

    @Override
    public UserInfoResponse changeStatus(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)) ;
        if(user.getStatus() == 1){
            user.setStatus(0);
        }else{
            user.setStatus(1);
        }
        userRepository.save(user);
        UserInfoResponse userInfoResponse = new UserInfoResponse();

        userInfoResponse.setId(user.getId());
        userInfoResponse.setPhone(user.getPhone());
        userInfoResponse.setEmail(user.getEmail());
        userInfoResponse.setFullName(user.getFullName());
        userInfoResponse.setNickName(user.getNickName());
        userInfoResponse.setBirthday(user.getBirthday());
        userInfoResponse.setGender(user.getGender());
        userInfoResponse.setAvatarUrl(user.getAvatarUrl());
        userInfoResponse.setStatus(user.getStatus());

        return userInfoResponse;
    }
}
