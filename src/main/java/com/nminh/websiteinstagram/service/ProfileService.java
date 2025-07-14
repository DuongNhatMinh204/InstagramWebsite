package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.model.request.ProfileRequestDTO;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;
import com.nminh.websiteinstagram.model.response.ProfileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {
    List<PostResponseDTO> getAllPosts();

    Object upLoadAvt(String pathUrl);

    Object getFollowings();
    ProfileDTO updateProfile(ProfileRequestDTO requestDTO);

}
