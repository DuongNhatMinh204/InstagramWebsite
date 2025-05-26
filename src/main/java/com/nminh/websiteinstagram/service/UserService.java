package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.SearchByNickNameDto;
import com.nminh.websiteinstagram.model.response.UserSuggestionDto;

import java.util.List;


public interface UserService {
    public User createUser(UserRegisterDTO userRegisterDTO);
    public User loginUser(UserLoginDTO userLoginDTO);

    List<SearchByNickNameDto> searchUsersByNickname(String nickName);


    List<UserSuggestionDto> getUnfollowedSuggestions(Long currentUserId, int limit);
}
