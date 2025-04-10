package com.nminh.websiteinstagram.mapper;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponseDTO toPostResponseDTO(Post post);
}
