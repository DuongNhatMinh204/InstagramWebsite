package com.nminh.websiteinstagram.mapper;

import com.nminh.websiteinstagram.entity.Like;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.response.LikeResponseDTO;
import com.nminh.websiteinstagram.model.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

public class LikeMapper {
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    LikeResponseDTO likeToLikeResponseDTO(Like like){
        LikeResponseDTO likeResponseDTO = new LikeResponseDTO();
        likeResponseDTO.setId(like.getId());
        User user = like.getUser();
        UserResponseDTO userResponseDTO = userMapper.toUserReponseDTO(user);
        likeResponseDTO.setUser(userResponseDTO);
        return likeResponseDTO;
    }
}
