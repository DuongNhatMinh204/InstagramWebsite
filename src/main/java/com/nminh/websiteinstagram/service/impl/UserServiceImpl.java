package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.mapper.UserMapper;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;


    @Override
    public User createUser(UserRegisterDTO userRegisterDTO) {

        if(userRepository.existsByPhone(userRegisterDTO.getPhone())){
            throw new AppException(ErrorCode.USER_EXISTS) ;
        }

        if(!userRegisterDTO.getPassword().equals(userRegisterDTO.getPasswordConfirm())){
            throw new AppException(ErrorCode.PASSSWORD_MISMATCH) ;
        }
        User user = userMapper.toUser(userRegisterDTO);

        return userRepository.save(user);

    }

    @Override
    public User loginUser(UserLoginDTO userLoginDTO) {
        if(!userRepository.existsByPhone(userLoginDTO.getPhone())){
            throw new AppException(ErrorCode.USER_NOT_EXISTS) ;
        }
        User user = userRepository.findByPhone(userLoginDTO.getPhone())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)) ;
        if(!user.getPassword().equals(userLoginDTO.getPassword())){
            throw new AppException(ErrorCode.PASSSWORD_MISMATCH) ;
        }

        return user;
    }
}
