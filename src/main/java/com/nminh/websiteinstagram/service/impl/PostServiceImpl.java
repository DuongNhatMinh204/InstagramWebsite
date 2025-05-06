package com.nminh.websiteinstagram.service.impl;

import com.nminh.websiteinstagram.Utils.SecurityUtil;
import com.nminh.websiteinstagram.entity.Follow;
import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.mapper.PostMapper;
import com.nminh.websiteinstagram.model.request.PostCreateDTO;
import com.nminh.websiteinstagram.model.response.PostResponseDTO;
import com.nminh.websiteinstagram.repository.PostRepository;
import com.nminh.websiteinstagram.repository.UserRepository;
import com.nminh.websiteinstagram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostMapper postMapper;

    @Override
    public PostResponseDTO createPost( PostCreateDTO postCreateDTO) {

        Long id = SecurityUtil.getCurrentUserId();

        Post post = new Post();
        post.setContent(postCreateDTO.getContent());
        post.setImageUrl(postCreateDTO.getImg_url());
        User user = userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS)) ;
        post.setUser(user);
        postRepository.save(post) ;
        PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(post);
        return postResponseDTO;
    }

    @Override
    public List<PostResponseDTO> getAllPostsFromFollower() {

        Long userId = SecurityUtil.getCurrentUserId();

        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        // lấy user
        User user = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));

        for(Post post : user.getPosts()){
            PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(post);
            postResponseDTO.setUrl_avatar(user.getAvatarUrl()); // avt nguoi dang
            postResponseDTO.setNickname(user.getNickName());
            postResponseDTO.setImageUrl(post.getImageUrl());
            postResponseDTOS.add(postResponseDTO);
        }
        // lấy danh sách người mình follow
        List<User> userList = new ArrayList<>();
        List<Follow> followList = user.getFollowing();
        for (Follow follow : followList) {
            User userFollowed = follow.getFollowing() ; // lấy người được theo dõi
            userList.add(userFollowed); // cho vào danh sách người mình theo dõi
        }

        // lấy danh sách bài viết của tất cả người mình theo dõi
        for(User userFollowed : userList) { // duyệt từng người mình theo dõi
            if(userFollowed.getPosts().isEmpty()) continue; // nếu user không có thì bỏ qua

            for(Post post : userFollowed.getPosts()) { // duyệt từng bài viết của người đó
                post.setTotalLikes(post.getLikes().size());
                PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(post);

                postResponseDTO.setUrl_avatar(userFollowed.getAvatarUrl()); // avt nguoi dang
                postResponseDTO.setNickname(userFollowed.getNickName());
                postResponseDTO.setImageUrl(post.getImageUrl());
                postResponseDTOS.add(postResponseDTO);
            }
        }

        return postResponseDTOS;
    }
}
