package com.nminh.websiteinstagram.mapper;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.response.ProfileDTO;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ProfileMapper {

    public ProfileDTO toProfileDTO(User user) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setEmail(user.getEmail());
        profileDTO.setFullName(user.getFullName());
        profileDTO.setNickName(user.getNickName());
        profileDTO.setGender(user.getGender());
        profileDTO.setAvatarUrl(user.getAvatarUrl());

        if (user.getBirthday() != null) {
            profileDTO.setBirthday(user.getBirthday().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }

        // Gán các trường profile đã được gộp trực tiếp từ User
        profileDTO.setAddress(user.getAddress());
        profileDTO.setWorkplace(user.getWorkplace());
        profileDTO.setMaritalStatus(user.getMaritalStatus());
        profileDTO.setEducation(user.getEducation());

        return profileDTO;
    }
}