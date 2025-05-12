package com.nminh.websiteinstagram.service;

public interface LikeService {
    String likePost(Long postId);
    String unlikePost(Long postId);

    boolean hasUserLikedPost(Long postId);
    Integer countLike(Long postId) ;
}
