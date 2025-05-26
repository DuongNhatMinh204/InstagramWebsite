package com.nminh.websiteinstagram.mapper;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.model.response.SearchByNickNameDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SearchByNickNameMapper {
    public abstract SearchByNickNameDto userToUserDto(User user);
    public abstract List<SearchByNickNameDto> usersToUserDtos(List<User> users);
}
