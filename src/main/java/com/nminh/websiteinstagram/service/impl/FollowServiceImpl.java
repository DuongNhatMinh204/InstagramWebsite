package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.entity.Follow;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.mapper.UserMapper;
import com.nminh.websiteinstagram.model.response.FollowerResponseDTO;
import com.nminh.websiteinstagram.model.response.UserReponseDTO;
import com.nminh.websiteinstagram.repository.FollowRepository;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String follow(Long userId, Long toFollowingUserId) {

        User userCurrent = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        User userFollowed = userRepository.findById(toFollowingUserId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));

        Follow follow = new Follow();
        follow.setFollower(userCurrent);
        follow.setFollowing(userFollowed);

        followRepository.save(follow);

        userCurrent.getFollowing().add(follow); // thêm vào danh sách người mình đang theo dõi
        userFollowed.getFollowers().add(follow); // thêm vào danh sách những người theo dõi mình

        userRepository.save(userCurrent);
        userRepository.save(userFollowed);


        return "Follow Successfully";
    }

    @Override
    public FollowerResponseDTO getFollowers(Long userId) {
        FollowerResponseDTO followerResponseDTO = new FollowerResponseDTO();
        User user = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));

        List<UserReponseDTO> userReponseDTOS = new ArrayList<>();
        List<Follow> userFollowers = user.getFollowers();
        for (Follow follow : userFollowers) {
            User userFollower = follow.getFollower();
            UserReponseDTO userReponseDTO = userMapper.toUserReponseDTO(userFollower);
            userReponseDTOS.add(userReponseDTO);
        }
        followerResponseDTO.setTotal_followers(user.getFollowers().size());
        followerResponseDTO.setFollowers(userReponseDTOS);
        return followerResponseDTO;
    }
}
