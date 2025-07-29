package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
//
//    // Hoặc nếu bạn muốn lấy bài viết bằng User ID
//    List<Post> findByUser_Id(Long userId);
    Optional<Post> findById(Long id);
    List<Post> findByUserId(Long userId);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :postId")
    Optional<Post> findByIdFetchUser(@Param("postId") Long postId);


}
