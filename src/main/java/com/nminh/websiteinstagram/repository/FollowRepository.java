package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query(value = "SELECT COUNT(F) FROM Follow F WHERE F.following.id = :userId")
    int  getTotalFollowers(Long userId) ;


}
