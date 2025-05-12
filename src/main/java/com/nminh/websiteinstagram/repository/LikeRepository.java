package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.Like;
import com.nminh.websiteinstagram.entity.Post;
import com.nminh.websiteinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndPost(User user, Post post);
    Integer countByPost(Post post);
}
