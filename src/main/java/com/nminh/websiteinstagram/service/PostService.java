package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.model.request.PostCreateDTO;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;

import java.util.List;

public interface PostService {
    public PostResponseDTO createPost(Long id, PostCreateDTO postCreateDTO);
    public List<PostResponseDTO> getAllPostsFromFollower(Long userId);
}
