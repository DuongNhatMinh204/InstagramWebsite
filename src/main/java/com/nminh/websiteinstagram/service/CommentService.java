package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.model.request.CommentCreateDTO;

public interface CommentService {
    String addComment(Long postId, CommentCreateDTO commentCreateDTO );
}
