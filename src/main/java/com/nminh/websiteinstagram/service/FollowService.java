package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.model.response.FollowerResponseDTO;

public interface FollowService {
    String follow( Long toFollowingUserId);
    String unfollow(Long toUnfollowingUserId);
    FollowerResponseDTO getFollowers();
    FollowerResponseDTO getFollowing();
    Boolean checkFollowing( Long followingUserId);
}
