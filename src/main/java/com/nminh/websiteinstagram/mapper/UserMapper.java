package com.nminh.websiteinstagram.mapper;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.UserReponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterDTO userRegisterDTO) ;
    UserReponseDTO toUserReponseDTO(User user);
}
