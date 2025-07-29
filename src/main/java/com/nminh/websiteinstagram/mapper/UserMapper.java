package com.nminh.websiteinstagram.mapper;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.ProfileDTO;
import com.nminh.websiteinstagram.model.response.UserResponseDTO;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterDTO userRegisterDTO) ;
    UserResponseDTO toUserReponseDTO(User user);

    UserResponseDTO toUser(User userId);

    ProfileDTO toUserReponseDTO(Optional<User> optionalUser);
    ProfileDTO toUserprofileDTO(User user); // ✅ Đúng

}
