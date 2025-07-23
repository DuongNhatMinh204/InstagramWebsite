package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.response.SearchUserDTOResponse;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<SearchUserDTOResponse> searchUsers(String searchQuery) {
        List<SearchUserDTOResponse> users = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for(User user : userList) {
            SearchUserDTOResponse userDTOResponse = new SearchUserDTOResponse();
            if(user.getNickName().toLowerCase().contains(searchQuery.toLowerCase())) {
                userDTOResponse.setId(user.getId());
                userDTOResponse.setNickname(user.getNickName());
                userDTOResponse.setAvatar_url(user.getAvatarUrl());
                users.add(userDTOResponse);
            }
        }
        return users;
    }
}
