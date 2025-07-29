package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.PostCreateDTO;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;

import java.util.List;

public interface PostService {
     PostResponseDTO createPost( PostCreateDTO postCreateDTO);
     List<PostResponseDTO> getAllPostsFromFollower();
     List<PostResponseDTO> getAllPostByUserId(Long userId);
     PostResponseDTO getPostById(long id);

//     PostResponseDTO geUtserByPostId(long id);
//     User getUserBypostID(Long id);
}
