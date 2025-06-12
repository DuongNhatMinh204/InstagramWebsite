package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.entity.Follow;
import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.mapper.PostMapper;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;
import com.nminh.websiteinstagram.model.response.UserResponseDTO;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Override
    public List<PostResponseDTO> getAllPosts() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTS));
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        List<Post> posts = user.getPosts();
        for (Post post : posts) {
            post.setTotalLikes(post.getLikes().size());
            PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(post);
            postResponseDTO.setUrl_avatar(user.getAvatarUrl()); // avt nguoi dang
            postResponseDTO.setNickname(user.getNickName());
            postResponseDTO.setImageUrl(post.getImageUrl());
            postResponseDTOS.add(postResponseDTO);
        }
        return postResponseDTOS;

    }

    @Override
    public Object upLoadAvt(String pathUrl) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTS));

       user.setAvatarUrl(pathUrl);
       userRepository.save(user);
       return "ok";
    }

    @Override
    public Object getFollowings() {
        List<User> userResponseDTOS = new ArrayList<>();
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        List<Follow> follows = user.getFollowing();
        for (Follow following : follows) {
            User followingUser = userRepository.findById(following.getFollowing().getId()).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTS));
            userResponseDTOS.add(followingUser);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("data", userResponseDTOS);
        return result;
    }
}
