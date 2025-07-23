package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);

    // Hoặc nếu bạn muốn lấy bài viết bằng User ID
    List<Post> findByUser_Id(Long userId);
}
