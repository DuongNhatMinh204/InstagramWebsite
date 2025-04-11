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
import java.util.Objects;

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
        if(Objects.equals(userId, toFollowingUserId)){
            throw new AppException(ErrorCode.CANNOT_FOLLOW_YOURSELF) ;
        }

        User userCurrent = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        User userFollowed = userRepository.findById(toFollowingUserId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));

        // kiểm tra xem đã follow hay chưa -- nếu rồi không cho follow tiếp
        Follow isFollow = followRepository.findByFollowerAndFollowing(userCurrent, userFollowed);
        if(isFollow != null){
            throw new AppException(ErrorCode.CANNOT_REFOLLOW) ;
        }

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
        List<Follow> userFollowers = user.getFollowers(); // lấy ra danh sách những người follow mình
        for (Follow follow : userFollowers) {
            User userFollower = follow.getFollower();
            UserReponseDTO userReponseDTO = userMapper.toUserReponseDTO(userFollower);
            userReponseDTOS.add(userReponseDTO);
        }
        followerResponseDTO.setTotal_followers(user.getFollowers().size());
        followerResponseDTO.setFollowers(userReponseDTOS);
        return followerResponseDTO;
    }

    @Override
    public FollowerResponseDTO getFollowing(Long userId) {
        FollowerResponseDTO followerResponseDTO = new FollowerResponseDTO();
        User user = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        List<UserReponseDTO> userReponseDTOS = new ArrayList<>();
        List<Follow> userFollowings = user.getFollowing();
        for (Follow follow : userFollowings) {
            User userFollowing = follow.getFollowing();
            UserReponseDTO userReponseDTO = userMapper.toUserReponseDTO(userFollowing);
            userReponseDTOS.add(userReponseDTO);
        }
        followerResponseDTO.setTotal_followers(user.getFollowing().size());
        followerResponseDTO.setFollowers(userReponseDTOS);
        return followerResponseDTO;
    }
}
