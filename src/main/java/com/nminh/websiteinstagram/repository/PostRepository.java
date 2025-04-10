package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
