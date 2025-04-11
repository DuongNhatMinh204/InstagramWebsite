package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.model.response.FollowerResponseDTO;

public interface FollowService {
    String follow(Long userId, Long toFollowingUserId);
    String unfollow(Long userId, Long toUnfollowingUserId);
    FollowerResponseDTO getFollowers(Long userId);
    FollowerResponseDTO getFollowing(Long userId);
}
