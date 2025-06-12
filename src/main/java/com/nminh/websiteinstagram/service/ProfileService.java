package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.model.response.PostResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {
    List<PostResponseDTO> getAllPosts();

    Object upLoadAvt(String pathUrl);

}
