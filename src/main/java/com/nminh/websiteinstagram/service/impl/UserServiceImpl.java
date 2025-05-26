package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.mapper.SearchByNickNameMapper;
import com.nminh.websiteinstagram.mapper.UserMapper;
import com.nminh.websiteinstagram.model.request.UserLoginDTO;
import com.nminh.websiteinstagram.model.request.UserRegisterDTO;
import com.nminh.websiteinstagram.model.response.SearchByNickNameDto;
import com.nminh.websiteinstagram.model.response.UserSuggestionDto;
import com.nminh.websiteinstagram.repository.FollowRepository;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private SearchByNickNameMapper searchByNickNameMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User createUser(UserRegisterDTO userRegisterDTO) {

        if(userRepository.existsByPhone(userRegisterDTO.getPhone())){
            throw new AppException(ErrorCode.USER_EXISTS) ;
        }

        if(!userRegisterDTO.getPassword().equals(userRegisterDTO.getPasswordConfirm())){
            throw new AppException(ErrorCode.PASSSWORD_MISMATCH) ;
        }

        // mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        userRegisterDTO.setPassword(encodedPassword);

        User user = userMapper.toUser(userRegisterDTO);

        return userRepository.save(user);

    }

    @Override
    public User loginUser(UserLoginDTO userLoginDTO) {
        if(!userRepository.existsByPhone(userLoginDTO.getPhone())){
            throw new AppException(ErrorCode.USER_NOT_EXISTS) ;
        }
        User user = userRepository.findByPhone(userLoginDTO.getPhone())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)) ;
        //kiểm tra với mật khẩu mã hóa trong database
        if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.PASSSWORD_MISMATCH) ;
        }

        return user;
    }
    @Override
    public List<SearchByNickNameDto> searchUsersByNickname(String nickName) {
        // Tìm kiếm chính xác (không phân biệt hoa thường)
        List<User> exactMatch = userRepository.findByNickNameContainingIgnoreCase(nickName);
        // Tìm kiếm gần đúng (chứa chuỗi, không phân biệt hoa thường)
        List<User> partialMatches = userRepository.findByNickNameContainingIgnoreCase(nickName);
        Set<User> combinedResults = new LinkedHashSet<>();
        combinedResults.addAll(exactMatch);
        combinedResults.addAll(partialMatches);

        return searchByNickNameMapper.usersToUserDtos(new ArrayList<>(combinedResults));

    }
    @Override
    public List<UserSuggestionDto> getUnfollowedSuggestions(Long currentUserId, int limit) {
        // 1. Lấy danh sách ID đã follow
//        List<Long> followedIds = followRepository.findFollowingIdsByFollowerId(currentUserId);
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(currentUserId);

        // 2. Thêm cả ID bản thân
        followingIds.add(currentUserId);

        // 3. Lấy ngẫu nhiên user chưa follow
        List<User> suggestions = userRepository.findRandomUsersNotInIds(followingIds, limit);

        // 4. Map sang DTO
        return suggestions.stream()
                .map(user -> userMapper.toSuggestionDto(user, currentUserId))
                .collect(Collectors.toList());
    }
}
