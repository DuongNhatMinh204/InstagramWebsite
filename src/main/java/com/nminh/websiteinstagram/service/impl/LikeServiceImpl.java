package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.entity.Like;
import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.repository.LikeRepository;
import com.nminh.websiteinstagram.repository.PostRepository;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String likePost(Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();

        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if(likeRepository.existsByUserAndPost(user, post)) {
            throw new AppException(ErrorCode.CANNOT_LIKE_TWO_TIMES) ;
        }
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

        post.getLikes().add(like);
        post.setTotalLikes(post.getTotalLikes() + 1);
        postRepository.save(post);

        return "Like Success";
    }

    @Override
    public String unlikePost(Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        Like like = likeRepository.findByUserAndPost(user,post).orElseThrow(()->new AppException(ErrorCode.CANNOT_FOUND_LIKE)) ;

        likeRepository.delete(like);
        post.getLikes().remove(like);
        postRepository.save(post);
        return "Unlike Success";
    }
}
