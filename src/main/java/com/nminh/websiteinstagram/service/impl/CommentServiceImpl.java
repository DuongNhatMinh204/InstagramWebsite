package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.constant.Constants;
import com.nminh.websiteinstagram.entity.Comment;
import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.model.request.CommentCreateDTO;
import com.nminh.websiteinstagram.repository.CommentRepository;
import com.nminh.websiteinstagram.repository.PostRepository;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public String addComment( Long postId, CommentCreateDTO commentCreateDTO) {
        Long userId = SecurityUtil.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTS));
        Post post = postRepository.findById(postId).orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(commentCreateDTO.getContent());
        comment.setNickName(user.getNickName());
        commentRepository.save(comment);

        post.getComments().add(comment);
        post.setTotalComments(post.getTotalComments() + 1);
        postRepository.save(post);

        return Constants.SUCCESS;
    }
}
