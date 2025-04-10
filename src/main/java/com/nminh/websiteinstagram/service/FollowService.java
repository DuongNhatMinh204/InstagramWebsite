package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.model.response.FollowerResponseDTO;

public interface FollowService {
    String follow(Long userId, Long toFollowingUserId);
    FollowerResponseDTO getFollowers(Long userId);
}
