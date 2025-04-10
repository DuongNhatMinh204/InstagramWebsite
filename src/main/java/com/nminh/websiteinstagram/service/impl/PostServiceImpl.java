package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.mapper.PostMapper;
import com.nminh.websiteinstagram.model.request.PostCreateDTO;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;
import com.nminh.websiteinstagram.repository.PostRepository;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostMapper postMapper;

    @Override
    public PostResponseDTO createPost(Long id, PostCreateDTO postCreateDTO) {
        Post post = new Post();
        post.setContent(postCreateDTO.getContent());
        post.setImageUrl(postCreateDTO.getImg_url());
        User user = userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS)) ;
        post.setUser(user);
        postRepository.save(post) ;
        PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(post);
        return postResponseDTO;
    }
}
