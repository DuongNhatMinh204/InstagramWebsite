package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
