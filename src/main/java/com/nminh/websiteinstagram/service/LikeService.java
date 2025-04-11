package com.nminh.websiteinstagram.service;

public interface LikeService {
    String likePost(Long postId,Long userId);
    String unlikePost(Long postId,Long userId);
}
