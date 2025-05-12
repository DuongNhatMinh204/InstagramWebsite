package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.model.request.PostCreateDTO;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;

import java.util.List;

public interface PostService {
     PostResponseDTO createPost( PostCreateDTO postCreateDTO);
     List<PostResponseDTO> getAllPostsFromFollower();
}
