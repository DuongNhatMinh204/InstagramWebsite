package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.entity.Follow;
import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.mapper.PostMapper;
import com.nminh.websiteinstagram.mapper.ProfileMapper; // Import ProfileMapper mới
import com.nminh.websiteinstagram.model.request.ProfileRequestDTO;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;
import com.nminh.websiteinstagram.model.response.ProfileDTO;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final ProfileMapper profileMapper; // Inject ProfileMapper mới

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

    @Override
    public ProfileDTO updateProfile(ProfileRequestDTO requestDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        // Cập nhật các trường User và Profile trực tiếp trong User entity từ requestDTO
        if (requestDTO.getFullName() != null) {
            user.setFullName(requestDTO.getFullName());
        }
        if (requestDTO.getNickName() != null) {
            user.setNickName(requestDTO.getNickName());
        }
        if (requestDTO.getGender() != null) {
            user.setGender(requestDTO.getGender());
        }
        if (requestDTO.getBirthday() != null && !requestDTO.getBirthday().isEmpty()) {
            try {
                LocalDate birthday = LocalDate.parse(requestDTO.getBirthday(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                user.setBirthday(birthday);
            } catch (DateTimeParseException e) {
                throw new AppException(ErrorCode.INVALID_DATE_FORMAT);
            }
        }
        if (requestDTO.getAddress() != null) {
            user.setAddress(requestDTO.getAddress());
        }
        if (requestDTO.getWorkplace() != null) {
            user.setWorkplace(requestDTO.getWorkplace());
        }
        if (requestDTO.getMaritalStatus() != null) {
            user.setMaritalStatus(requestDTO.getMaritalStatus());
        }
        if (requestDTO.getEducation() != null) {
            user.setEducation(requestDTO.getEducation());
        }

        userRepository.save(user); // Lưu User đã cập nhật

        // Sử dụng ProfileMapper để ánh xạ User sang ProfileDTO
        return profileMapper.toProfileDTO(user);
    }
    @Override
    public List<PostResponseDTO> getPostsOfUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)); // Xử lý nếu user không tồn tại
        List<Post> posts = user.getPosts(); // Lấy danh sách bài viết từ User entity

        return posts.stream()
                .map(postMapper::toPostResponseDTO)
                .collect(Collectors.toList());
    }
}