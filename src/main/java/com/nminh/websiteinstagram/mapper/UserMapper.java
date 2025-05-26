package com.nminh.websiteinstagram.mapper;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.UserResponseDTO;
import com.nminh.websiteinstagram.model.response.UserSuggestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterDTO userRegisterDTO) ;
    UserResponseDTO toUserReponseDTO(User user);
    @Mapping(target = "following", expression = "java(isFollowing(user, currentUserId))")
    UserSuggestionDto toSuggestionDto(User user, @Param("currentUserId") Long currentUserId);

    default boolean isFollowing(User user, Long currentUserId) {
        if (user.getFollowers() == null || currentUserId == null) {
            return false;
        }
        return user.getFollowers().stream()
                .anyMatch(follow -> follow.getFollower().getId().equals(currentUserId));
    }
}
